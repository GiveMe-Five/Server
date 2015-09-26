
### EndPoint

**getNewsList**

参数

|名称|解释|
|---|---|
|start|默认为0|
|limit|默认为10|

返回

``JSONArray(结构如下)``

	[
		{
			"id":"1",
			"title":"大新闻",
			"type":"1",//军事类（参见NewsBean.java）
			"url":"http://blabla.com/blabla"
		}
	]

************************

**getNews**

参数

|名称|解释|
|---|---|
|id||

返回

	{
		"id":"1",
		"title":"大新闻",
		"type":"1",//军事类（参见NewsBean.java）
		"url":"http://blabla.com/blabla",
		"content":"这是大新闻的内容"
	}