package com.daixiaobao.other;

import com.daixiaobao.concern.ResponseMineConcern;
import com.daixiaobao.concern.ResponseMineConcern.Group;
import com.daixiaobao.detailProdcut.ResponseDetailProduct;
import com.daixiaobao.proxy.list.ResponseProxyList;
import com.wookii.protocollManager.ProtocolManager;

public class Test {

	public static ResponseMineConcern createTestData(ResponseMineConcern obj) {
		// TODO Auto-generated method stub
		obj = new ResponseMineConcern();
		obj.setErrorCode(0);
		String[] urls = new String[] {
				"http://www.tianshui.com.cn/Files238/BeyondPic/2013-7/9/Img5212364_n.jpg",
				"http://i2.itc.cn/20120903/2e2f_c51c88c1_e728_52e5_65ef_388af6e4bbfd_1.jpg",
				"http://i0.itc.cn/20130304/2e2f_711f0455_ac50_6594_6687_b4fc02dd9b7a_1.jpg",
				"http://www.wallcoo.com/flower/waterdrop/images/Close_up_waterdrop_on_leaf_waterdrop.jpg",
				"http://img.ivsky.com/img/tupian/slides/201312/17/weixiao-016.jpg",
				"http://2f.zol-img.com.cn/product/55_500x2000/15/ceiwECOKBZLps.jpg",
				"http://img.gamespot.com.cn/product/site/1/3/155/124bf6ae17228178d309b15bd6b8ccca_960.jpg",
				"http://images.enet.com.cn/2013/4/15/1363900838518.jpg",
				"http://news.xinhuanet.com/photo/2013-05/12/124698927_11n.jpg",
				"http://ww2.sinaimg.cn/mw600/4e8f7855jw1dtxf1skolzj.jpg" };
		// break;
		Group[] group = new Group[10];
		for (int i = 0; i < 10; i++) {
			Group item = obj.new Group();
			item.setCode("A177890" + i);
			item.setDescription("android的edittext默认不获得焦点曾程: 也可以把这个代码放到EditTex的父类按键里面,让他的父类得到优先得到焦点");
			item.setPrice("199" + i);
			item.setImageCount(String.valueOf(i));
			item.setImageUrl(urls[i]);
			item.setStatus(String.valueOf(i % 2));
			group[i] = item;
		}
		obj.setGroup(group);
		return obj;
	}

	public static ResponseDetailProduct creatTestDetailMessage(
			ResponseDetailProduct obj) {
		// TODO Auto-generated method stub
		obj = new ResponseDetailProduct();
		obj.setAddress("宇宙个中心");
		obj.setCompany("国产大亨不负责任有限公司");
		obj.setDescription("JSON数据作为目前网络中主流的数据传输格式之一，"
				+ "应用十分广泛。在日常的程序开发或调试过程中，或者抓取其他网站的数据进行分析处理中，"
				+ "都需要我们对JSON数据的结构进行了解，并定位我们所需的数据。而一些常用的抓包与分析工具，"
				+ "如HttpWatch并不能直观的查看JSON数据。因此本工具弥补了此点不足。此外，"
				+ "本工具中提供了对JSON数据的美化及压缩功能，为用户在使用JSON数据时提供了便利。"
				+ "如果你还不清楚什么是JSON数据，在工具上有一个“什么是JSON数据”的“小贴士”，方便大家进行了解。");
		obj.setEmail("wookii@wookii.com");
		obj.setQq("254362772");
		obj.setTel("13848383838");
		obj.setWx("go_go+go");
		obj.setErrorCode(ProtocolManager.ERROR_CODE_ZORE);
		obj.setImageUrls(new String[] {
				"http://www.tianshui.com.cn/Files238/BeyondPic/2013-7/9/Img5212364_n.jpg",
				"http://i2.itc.cn/20120903/2e2f_c51c88c1_e728_52e5_65ef_388af6e4bbfd_1.jpg",
				"http://i0.itc.cn/20130304/2e2f_711f0455_ac50_6594_6687_b4fc02dd9b7a_1.jpg",
				"http://www.wallcoo.com/flower/waterdrop/images/Close_up_waterdrop_on_leaf_waterdrop.jpg",
				"http://img.ivsky.com/img/tupian/slides/201312/17/weixiao-016.jpg",
				"http://2f.zol-img.com.cn/product/55_500x2000/15/ceiwECOKBZLps.jpg",
				"http://img.gamespot.com.cn/product/site/1/3/155/124bf6ae17228178d309b15bd6b8ccca_960.jpg",
				"http://images.enet.com.cn/2013/4/15/1363900838518.jpg",
				"http://news.xinhuanet.com/photo/2013-05/12/124698927_11n.jpg",
				"http://ww2.sinaimg.cn/mw600/4e8f7855jw1dtxf1skolzj.jpg" });
		obj.setMessage("加载成功");
		obj.setPrice("1999");
		obj.setProxyStatus("0");
		return obj;
	}

	public static ResponseProxyList createProxyListTestData(
			ResponseProxyList obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
