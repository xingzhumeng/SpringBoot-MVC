package com.example.controller;

import com.example.entity.Book;
import com.example.service.IBookService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Relief
 * @since 2025-04-09
 */
@RestController
@RequestMapping("/book")
public class BookController {
	@Autowired
	private IBookService bookService;

	/**
	 * 获取所有作者
	 * @return
	 */
	@RequiresPermissions("case/books")
	@GetMapping("/authors")
	public Object getAuthors() {
		System.out.println("查询所有作者");
		return bookService.getAuthors();
	}

	/**
	 * 条件分页查询
	 * @param data
	 * @return
	 */
	@RequiresPermissions("case/books")
	@PostMapping("/all")
	public Object getBooksByParam(@RequestBody Map<String, Object> data) {
		System.out.println("条件查询:" + data);
		return bookService.getAllBooks(data);
	}

	/**
	 * 新增书籍
	 * @param book
	 * @return
	 */
	@RequiresPermissions("case/books/add")
	@PostMapping
	public Object addBook(@RequestBody Book book) {
		System.out.println("新增:" + book);
		return bookService.addBook(book);
	}

	/**
	 * 根据id查询书籍
	 * @param id
	 * @return
	 */
	@RequiresPermissions("case/books/query")
	@GetMapping("/{id}")
	public Object getBook(@PathVariable int id) {
		System.out.println("获取:" + id);
		return bookService.getBookById(id);
	}

	/**
	 * 更新书籍
	 * @param book
	 * @return
	 */
	@RequiresPermissions("case/books/update")
	@PutMapping
	public Object updateBook(@RequestBody Book book) {
		System.out.println("更新:" + book);
		return bookService.updateBook(book);
	}

	/**
	 * 伪删除书籍
	 * @param id
	 * @return
	 */
	@RequiresPermissions("case/books/delete")
	@DeleteMapping("/{id}")
	public Object deleteBook(@PathVariable int id) {
		System.out.println("删除:" + id);
		return bookService.deleteBook(id);
	}

	/**
	 * 批量伪删除书籍
	 * @param ids
	 * @return
	 */
	@RequiresPermissions("case/books/delete")
	@DeleteMapping
	public Object deleteBooks(@RequestBody int[] ids) {
		System.out.println("批量删除:" + Arrays.toString(ids));
		return bookService.deleteBooks(ids);
	}
}
