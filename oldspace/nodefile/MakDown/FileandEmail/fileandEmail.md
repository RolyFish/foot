# File

对于文件上传，浏览器在上传的过程中是将文件以流的形式提交到服务器端的。

依赖jar包：common-fileupload  common-io

1、为保证服务器的安全，上传的文件应放在外界无法访问的目录下，如WEN-INF。

2、为防止同名文件产生覆盖现象，要为文件指定一个唯一的文件名。

3、要对上传文件的大小进行限制。

4、限制上传文件的类型，收到文件时，判断文件名十分合法。

ServletFileUpload类：负责处理上传的文件数据，并将表单中的每个输入项封装成一个FileItem对象

方法：isMultipartContent(req) 判断上传的表单是（false）普通表单还是（true）带文件的表单

方法：parseRequest(req) 将通过表单中每一个HTML标签提交的数据封装成一个FileItem对象，然后以List列表的形式返回。使用该方法处理上传文件简单易用。
