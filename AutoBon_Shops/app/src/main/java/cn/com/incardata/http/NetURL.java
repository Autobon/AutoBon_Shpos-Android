package cn.com.incardata.http;

public class NetURL {
	/** 2017-05-04修改正式服务器基地址 */
	public final static String BASE_URL = "http://47.93.17.218:12345/api/mobile/";
	public final static String IP_PORT = "http://47.93.17.218:12345";
	public static final String PUB = "http://47.93.17.218:12345/api/pub/";

//	/** 一期正式服务器基地址 */
//	public final static String BASE_URL = "http://121.40.219.58:8000/api/mobile/";
//	public final static String IP_PORT = "http://121.40.219.58:8000";
//	public static final String PUB = "http://121.40.219.58:8000/api/pub/";
//
//	/** 二期电脑地址 */
//	public final static String BASE_URL = "http://10.0.12.148:12345/api/mobile/";
//	public final static String IP_PORT = "http://10.0.12.148:12345";
//	public static final String PUB = "http://10.0.12.148:12345/api/pub/";
//	/** 二期测试服务器基地址 */
//	public final static String BASE_URL = "http://dev.incardata.com.cn:12345/api/mobile/";
//	public final static String IP_PORT = "http://dev.incardata.com.cn:12345";
//	public static final String PUB = "http://dev.incardata.com.cn:12345/api/pub/";

//	public final static String BASE_URL = "http://10.0.12.191:12345/api/mobile/";
//	public final static String IP_PORT = "http://10.0.12.191:12345";
//	public static final String PUB = "http://10.0.12.191:12345/api/pub/";

	/** 发送验证短信 */
	public static final String VERIFY_SMS = PUB + "verifySms";
	/** 同步个推CID到后台 */
	public static final String PUSH_ID = BASE_URL + "coop/pushId";
	/** 注册 */
	public static final String REGISTER_URL = BASE_URL + "coop/register";
	/** 登录 */
	public static final String LOGIN_URL = BASE_URL + "coop/login";
	/** 上传营业执照副本照片 */
	public static final String BUSNIESS_LICENSE_URL = BASE_URL + "coop/bussinessLicensePic";
	/** 上传法人身份证正面照 */
	public static final String CORPORATION_PIC_URL  = BASE_URL + "coop/corporationIdPicA";
	/** 商户资料提交认证 */
	public static final String CORPORATION_CHECK_URL = BASE_URL + "coop/check";

	public static final String CORPORATION_INFO_URL = BASE_URL + "coop/getCoop";

	/** 创建订单 */
	public static final String CREATE_ORDER = BASE_URL + "coop/order/createOrder";
	/** 未完成订单列表 */
	public static final String LIST_UNFINISHED = BASE_URL + "coop/order/listUnfinished";
	/** 上传订单照片 */
	public static final String UPLOAD_PHOTO = BASE_URL + "coop/order/uploadPhoto";
	/** 查看技师详情 */
	public static final String GET_TECHNICIAN = BASE_URL + "coop/technician/getTechnician";
	/** 商户订单总数 */
	public static final String ORDER_COUNT = BASE_URL + "coop/order/orderCount";
	/** 已完成订单列表 */
	public static final String LIST_FINISHED = BASE_URL + "coop/order/listFinished";
	/** 未评论列表 */
	public static final String LIST_UNCOMMENT = BASE_URL + "coop/order/listUncomment";
	/** 评价 */
	public static final String COMMENT = BASE_URL + "coop/order/comment";
	/** 找回密码 */
	public static final String RESET_PASSWORD = BASE_URL + "coop/resetPassword";
	/** 修改密码 */
	public static final String CHANGE_PASSWORD = BASE_URL + "coop/changePassword";
	/** 所有业务员 */
	public static final String GET_SALE_LIST = BASE_URL + "coop/getSaleList";
	/** 业务员离职 */
	public static final String SALE_FIRED = BASE_URL + "coop/saleFired";
	/** 增加业务员 */
	public static final String ADD_ACCOUNT = BASE_URL + "coop/addAccount";
	/** 通知消息 */
	public static final String MESSAGE = BASE_URL + "coop/message";
	/** 商户审核信息 */
	public static final String COOP_CHECK_RESULT = BASE_URL + "coop/coopCheckResult";
	/** 查询技师 */
	public static final String SEARCH = BASE_URL + "coop/technician/search";
	/** 订单指定技师 */
	public static final String APPOINT = BASE_URL + "coop/order/appoint";

	/** 撤单 */
	public static String getRevokeOrder(int orderId){
		return BASE_URL + "coop/order/" + orderId + "/cancel";
	}
	/** 修改员工账户 */
	public static String getSalemanAccount(int accountID){
		return BASE_URL + "coop/account/" + accountID;
	}


	/**二期
	 * ========================================================================================
	 */
	/** 上传营业执照副本照片 */
	public static final String BUSNIESS_LICENSE_URLV2 = BASE_URL + "coop/merchant/bussinessLicensePic";

	/** 商户资料提交认证 */
	public static final String CORPORATION_CHECK_URLV2 = BASE_URL + "coop/merchant/certificate";

	/** 注册 */
	public static final String REGISTER_URLV2 = BASE_URL + "coop/merchant/register";

	/** 登录 */
	public static final String LOGIN_URLV2 = BASE_URL + "coop/merchant/login";

	/** 发送验证短信 */
	public static final String VERIFY_SMSV2 = PUB + "v2/verifySms";

	/** 商户审核信息 */
	public static final String COOP_CHECK_RESULTV2 = BASE_URL + "coop/merchant/coopCheckResult";

	/** 找回密码 */
	public static final String RESET_PASSWORDV2 = BASE_URL + "coop/resetPassword";

	/** 上传订单照片 */
	public static final String UPLOAD_PHOTOV2 = BASE_URL + "coop/merchant/order/uploadPhoto";

	/** 创建订单 */
	public static final String CREATE_ORDERV2 = BASE_URL + "coop/merchant/order";

	/** 查询技师列表 */
	public static final String SEARCH_LISTV2 = BASE_URL + "coop/merchant/technician/distance";

	/** 查询技师 */
	public static final String SEARCHV2 = BASE_URL + "coop/merchant/technician/assign";

	/** 订单指定技师 */
	public static final String APPOINTV2 = BASE_URL + "coop/merchant/order/appoint";

	/** 未完成、已完成订单列表 */
	public static final String LIST_UNFINISHEDV2 = BASE_URL + "coop/merchant/order";

	/**获取订单详情 */
	public static String getOrderInfo(int orderId){
		return LIST_UNFINISHEDV2 + "/" + orderId;
	}

	/** 撤单 */
	public static String getRevokeOrderV2(int orderId){
		return BASE_URL + "coop/merchant/order/" + orderId + "/cancel";
	}

	/** 根据订单ID获取技师信息 */
	public static String getTechMessageV2(int orderId){
		return BASE_URL + "coop/merchant/order/" + orderId + "/technician";
	}

	/** 根据技师ID获取技师信息 */
	public static String getTechIdMessageV2(int techId){
		return BASE_URL + "coop/merchant/technician/" + techId;
	}

	/** 评价 */
	public static final String COMMENTV2 = BASE_URL + "coop/merchant/order/comment";

	/**
	 * 查询已收藏的技师列表
	 * */
	public static final String SELECTCOLLECTIONTECHNICIAN = BASE_URL + "coop/favorite/technician";

	/**
	 * 移除已收藏的技师列表
	 * */
	public static final String delectCollectionTechnician(int id){
		return BASE_URL + "coop/favorite/technician/" + id;
	}

}
