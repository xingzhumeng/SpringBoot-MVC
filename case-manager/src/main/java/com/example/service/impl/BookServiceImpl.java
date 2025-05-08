package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.entity.Book;
import com.example.mapper.BookMapper;
import com.example.service.IBookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.util.ResultUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Relief
 * @since 2025-04-09
 */
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements IBookService {

	/* 获取所有作者 */
	@Override
	public Object getAuthors() {
		QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("distinct author");
		return ResultUtil.success("查询成功", getBaseMapper().selectObjs(queryWrapper));
	}

	/* 书籍条件分页查询 */
	@Override
	public Object getAllBooks(Map<String, Object> data) {
		// 设置分页信息，如果没有传递，则使用默认值
		int currentPage = data.get("currentPage") == null ?
				1 : (int) data.get("currentPage");
		int pageSize = data.get("pageSize") == null ?
				10 : (int) data.get("pageSize");
		Page<Book> page = new Page<>(currentPage, pageSize);
		// 获取查询条件
		Map<String, Object> params = (Map<String, Object>)data.get("params");
		// 根据查询条件构造动态sql
		QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
		if (params != null && !params.isEmpty()) {
			queryWrapper.lambda()
					.eq(Book::getIsActive, 1)
					.like(notNull(params.get("bookName")),
							Book::getBookName, params.get("bookName"))
					.eq(notNull(params.get("author")),
							Book::getAuthor, params.get("author"))
					.gt(notNull(params.get("begin")),
							Book::getPublished, params.get("begin"))
					.lt(notNull(params.get("end")),
							Book::getPublished, params.get("end"));
		}
		page = getBaseMapper().selectPage(page, queryWrapper);
		return ResultUtil.success("查询成功", page);
	}

	/* 判断条件是否不为空 */
	private static boolean notNull(Object obj) {
		return obj != null && !"".equals(obj);
	}

	/* 校验书名和作者是否为空：不符合为true  */
	private boolean validateBook(Book book) {
		return book.getBookName() == null || book.getBookName().isEmpty()
				|| book.getAuthor() == null || book.getAuthor().isEmpty();
	}

	/* 查找与当前书名相同的非当前书籍数据 */
	private Book getBookByBookNameButNotThis(Book book) {
		QueryWrapper<Book> queryWrapper = new QueryWrapper<>();
		queryWrapper.lambda()
				.eq(Book::getBookName, book.getBookName())
				.ne(book.getId() != null && !book.getId().equals(0),
						Book::getId, book.getId());
		return getBaseMapper().selectOne(queryWrapper);
	}

	/* 新增书籍 */
	@Override
	public Object addBook(Book book) {
		if (validateBook(book)) {
			return ResultUtil.error("书名和作者不能为空");
		}
		// 保证新增的时候不提供id
		book.setId(null);
		// 先判断书名是否重复
		if (getBookByBookNameButNotThis(book) != null) {
			return ResultUtil.error("已存在相同书名");
		}
		// 新增
		book.setPublished(LocalDateTime.now());
		if(save(book)) {
			return ResultUtil.success("新增书籍成功", null);
		}
		return ResultUtil.error("新增失败，请联系管理员");
	}

	/* 根据id查询书籍 */
	@Override
	public Object getBookById(int id) {
		return ResultUtil.success("查询成功", getById(id));
	}

	/* 更新书籍 */
	@Override
	public Object updateBook(Book book) {
		if (validateBook(book)) {
			return ResultUtil.error("书名和作者不能为空");
		}
		// 先判断书名是否重复
		if (getBookByBookNameButNotThis(book) != null) {
			return ResultUtil.error("已存在相同书名");
		}
		// 更新
		if (updateById(book)) {
			return ResultUtil.success("更新成功", null);
		}
		return ResultUtil.error("更新失败，请联系管理员");
	}

	/* 伪删除书籍 */
	@Override
	public Object deleteBook(int id) {
		Book book = new Book();
		book.setId(id);
		book.setIsActive(0);
		if (updateById(book)) {
			return ResultUtil.success("删除成功", null);
		}
		return ResultUtil.error("删除失败，请联系管理员");
	}

	/* 批量伪删除 */
	@Transactional(rollbackFor = Exception.class)
	@Override
	public Object deleteBooks(int[] ids) {
		List<Book> books = new ArrayList<>();
		for (Integer id : ids) {
			Book book = new Book();
			book.setId(id);
			book.setIsActive(0);
			books.add(book);
		}
		if (updateBatchById(books)) {
			return ResultUtil.success("删除成功", null);
		}
		return ResultUtil.error("删除失败，请联系管理员");
	}
}
