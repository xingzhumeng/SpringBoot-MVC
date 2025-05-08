package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.util.DateUtil;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author Relief
 * @since 2025-04-09
 */
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 书名
     */
    private String bookName;

    /**
     * 简介
     */
    private String description;

    /**
     * 作者
     */
    private String author;

    /**
     * 数量(本)
     */
    private Integer number;

    /**
     * 单价(元)
     */
    private Double price;

    /**
     * 发布时间
     */
    private LocalDateTime published;

    /**
     * 是否有效
     */
    private Integer isActive;

    /**
     * 用于页面显示的时间字符串，符合国人习惯
     */
    @TableField(exist = false)
    private String publishedDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDateTime getPublished() {
        return published;
    }

    /**
     * 设置发布时间，并同步更新 publishedDate
     * @param published
     */
    public void setPublished(LocalDateTime published) {
        this.published = published;
        setPublishedDate(DateUtil.toTimeStr(published));
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    /**
     * 设置用于页面显示的时间字符串
     * @param publishedDate
     */
    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", number=" + number +
                ", price=" + price +
                ", published=" + published +
                ", isActive=" + isActive +
                ", publishedDate='" + publishedDate + '\'' +
                '}';
    }
}
