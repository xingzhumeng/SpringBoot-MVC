package com.example.service;

import com.example.entity.Book;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Relief
 * @since 2025-04-09
 */
public interface IBookService extends IService<Book> {
	/* 获取所有作者 */
	Object getAuthors();

	/* 书籍条件分页查询 */
	Object getAllBooks(Map<String, Object> data);

	/* 新增书籍 */
	Object addBook(Book book);

	/* 根据id查询书籍 */
	Object getBookById(int id);

	/* 更新书籍 */
	Object updateBook(Book book);

	/* 伪删除书籍 */
	Object deleteBook(int id);

	/* 批量伪删除 */
	Object deleteBooks(int[] ids);
}
