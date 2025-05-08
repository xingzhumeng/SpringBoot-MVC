package com.example.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.nio.file.Paths;
import java.sql.Types;
import java.util.Collections;

/**
 * mybatis-plus代码生成器
 * 1.修改你自己的数据库url、username、password
 * 2.修改生成代码的文档注释中的作者
 * 3.运行主程序，自动生成代码吧！！！
 * 4.如果需要Controller类的注解为@RestController，请放开第45、59、60行的代码注释
 * @author 代码练习僧
 */
public class CodeGenerator {
	public static void main(String[] args) {
		FastAutoGenerator.create("jdbc:mysql://localhost:3306/case-manager",
						"root", "root")
				.globalConfig(builder -> builder
						.outputDir(Paths.get(System.getProperty("user.dir")) + "/src/main/java")
						.commentDate("yyyy-MM-dd")
						.author("Relief") // 文档注释-作者
				)
				.dataSourceConfig(builder ->
						builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
							int typeCode = metaInfo.getJdbcType().TYPE_CODE;
							if (typeCode == Types.SMALLINT) {
								// 自定义类型转换
								return DbColumnType.INTEGER;
							}
							return typeRegistry.getColumnType(metaInfo);
						})
				)
				.packageConfig(builder -> builder
						.parent("com.example")
						.entity("entity")
						.mapper("mapper")
						.service("service")
						.serviceImpl("service.impl")
						// .controller("controller")
						// .xml("mapper.xml")  // xml生成到mapper接口的包中
						.pathInfo(Collections.singletonMap(OutputFile.xml,
								Paths.get(System.getProperty("user.dir")) + "/src/main/resources/mybatis/mapper"))
				)
				.strategyConfig(builder -> builder
								.entityBuilder()
//                        .enableLombok()
								.javaTemplate("/templates/entity.java") // 设置实体类模板
								// .disable() // 禁用实体类生成
								.serviceBuilder()
								// .disableService() // 禁用 Service 层生成
								.serviceTemplate("/templates/service.java") // 设置 Service 模板
								.serviceImplTemplate("/templates/serviceImpl.java") // 设置 ServiceImpl 模板
								// .controllerBuilder()  // 添加controllerBuilder配置
								// .enableRestStyle()   // 使用@RestController
								.build()
				)
				.templateEngine(new VelocityTemplateEngine())
				.execute();
	}
}