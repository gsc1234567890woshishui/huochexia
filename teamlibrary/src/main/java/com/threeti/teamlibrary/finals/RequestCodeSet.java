package com.threeti.teamlibrary.finals;

/**
 * 网络访问接口标识集
 * Created by NieLiQin on 2016/6/22.
 */
public interface RequestCodeSet {

    public static final String RQ_LOGIN = "rq_login";//登录
    public static final String RQ_GET_CODE = "rq_get_code";//获取验证码
    public static final String RQ_GET_USER_INFO = "rq_get_user_info";//获取验证码
    public static final String RQ_CHECK_CODE = "rq_check_code";//验证验证码
    public static final String RQ_GET_UPDATE_PASSWORD = "rq_get_update_password";//修改密码



    public static final String RQ_GET_HOME_PICS = "rq_get_home_pics";//获取首页轮播图
    public static final String RQ_GET_HOME_ORDERS = "rq_get_home_orders";//获取首页订单
    public static final String RQ_GET_HOME_NEWS = "rq_get_home_news";//获取首页消息



    public static final String RQ_GET_NOTICE = "rq_get_notice";//获取公告信息
    public static final String RQ_GET_HURRY_LIST = "rq_get_hurry_list";//获取抢抢抢订单



    public static final String RQ_GET_BALANCE = "rq_get_balance";//获取司机余额
    public static final String RQ_GET_WITHDRAW = "rq_get_withdraw";//提现
    public static final String RQ_GET_CARDS = "rq_get_cards";//获取银行卡列表
    public static final String RQ_GET_IAEB = "rq_get_iaeb";//获取收支明细
    public static final String RQ_CHECK_WITHDRAW_CODE = "rq_check_withdraw_code";//验证提现验证码


    public static final String RQ_GET_ALL_ORDERS = "rq_get_all_orders";//获取全部订单
    public static final String RQ_GET_KIND_ORDERS = "rq_get_kind_orders";//获取各类订单
    public static final String RQ_SEARCH_ORDERS = "rq_search_orders";//搜索订单
    public static final String RQ_UPLOAD_SEARCH = "rq_upload_search";//上传搜索
    public static final String RQ_GET_ORDER_DETAIL = "rq_get_order_detail";//获取订单详情
    public static final String RQ_GET_REFUSE_ORDER = "rq_get_refuse_order";//拒单
    public static final String RQ_GET_ENABLE_CAR_LIST = "rq_get_enable_car_list";//获取可用车辆列表
    public static final String RQ_PAY_BY_BALANCE = "rq_pay_by_balance";//余额支付，//接单成功
    public static final String RQ_ENTER_SEND = "rq_enter_send";//确认送达
    public static final String RQ__UPLOAD_IMAGE = "rq_upload_image";//上传图片成功



    public static final String UPDATE_USER_INFO = "update_user_info";//修改个人信息
    public static final String RQ_GET_ALL_CAR_LIST = "rq_get_all_car_list";//获取车辆列表
    public static final String RQ_ADD_CAR = "rq_add_car";//添加车辆
    public static final String RQ_DELETE_CAR = "rq_delete_car";//删除车辆
    public static final String RQ_ADD_CARD = "rq_add_card";//添加银行卡
    public static final String RQ_DELETE_CARD = "rq_delete_card";//删除银行卡
    public static final String RQ_FEEDBACK = "rq_feedback";//用户反馈
    public static final String RQ_UPLOAD_CERTIFICATION = "rq_upload_certification";//上传证件
    public static final String RQ_GET_CERTIFICATION = "rq_get_certification";//获取证件




    public static final String RQ_GET_CHARGE = "rq_get_charge";//获取支付单据
    public static final String RQ_UPLOAD = "rq_upload";//上传文件
    public static final String RQ_DOWNLOAD = "rq_download";//下载文件
    public static final String RQ_GET_PROVINCE_LIST = "rq_get_province_list";//获取省份列表
    public static final String RQ_GET_CITY_LIST = "rq_get_city_list";//获取城市列表
    public static final String RQ_GET_ALL_CITY_LIST = "rq_get_all_city_list";//获取所有城市列表
    public static final String RQ_GET_AREA_LIST = "rq_get_area_list";//获取地区列表

    public static final String RQ_TRACK = "rq_upload";//上传经纬度
    public static final String RQ_MOBILE = "rq_mobilemodel";//上传手机型号

    public static final String RQ_RENEW = "rq_renew";// 访问版本更新的接口
    public static final String RQ_COAST = "rq_coast";// 访问版本更新的接口
    public static final String RQ_FLOW = "rq_flow";// 访问版本更新的接口

    public static final String RQ_AUDIT = "rq_audit";//车辆参数
    public static final String RQ_GET_ETCCHARE = "rq_get_etccharge";//etc充值
    public static final String RQ_GET_ETCRECODER = "rq_get_etcrecoder";//etc查询













}
