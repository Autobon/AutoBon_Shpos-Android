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
}
