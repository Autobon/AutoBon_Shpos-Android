package cn.com.incardata.http;

public class NetURL {
	/** 基地址 */
	public final static String BASE_URL = "http://121.40.157.200:12345/api/mobile/";
	public final static String IP_PORT = "http://121.40.157.200:12345";
	public static final String PUB = "http://121.40.157.200:12345/api/pub/";

	/** 发送验证短信 */
	public static final String VERIFY_SMS = PUB + "verifySms";
	/** 同步个推CID到后台 */
	public static final String PUSH_ID = "";

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
}
