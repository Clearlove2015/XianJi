package com.schytd.discount.ui;

import com.schytd.xianji.R;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.RenrenShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class ActivityShare extends FragmentActivity {
	protected UMSocialService mController;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 首先在您的Activity中添加如下成员变量
		mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	}
	/**
	 * 根据不同的平台设置不同的分享内容</br>
	 */
	public void setShareContent(String SharePath) {

		// 配置SSO
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());

		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
				ActivityShare.this, "100424468",
				"c7394704798a158208a74ab60104f0ba");
		qZoneSsoHandler.addToSocialSDK();
		mController
				.setShareContent("惠消费");

		// APP ID：201874, API
		// * KEY：28401c0964f04a72a14c812d6132fcef, Secret
		// * Key：3bf66e42db1e4fa9829b955cc300b737.
		RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(
				ActivityShare.this, "201874",
				"28401c0964f04a72a14c812d6132fcef",
				"3bf66e42db1e4fa9829b955cc300b737");
		mController.getConfig().setSsoHandler(renrenSsoHandler);

		UMImage localImage = new UMImage(ActivityShare.this,
				R.drawable.ic_launcher);
		// UMImage resImage = new UMImage(getActivity(), R.drawable.icon);
		// UMEmoji emoji = new UMEmoji(getActivity(),
		// "http://www.pc6.com/uploadimages/2010214917283624.gif");
		// UMEmoji emoji = new UMEmoji(getActivity(),
		// "/storage/sdcard0/emoji.gif");

		WeiXinShareContent weixinContent = new WeiXinShareContent();
		weixinContent
				.setShareContent("惠消费");
		weixinContent.setTitle("惠消费");
		weixinContent.setTargetUrl(SharePath);
		weixinContent.setShareMedia(localImage);
		mController.setShareMedia(weixinContent);

		// 设置朋友圈分享的内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia
				.setShareContent("惠消费");
		circleMedia.setTitle("惠消费");
		circleMedia.setShareMedia(localImage);
		// circleMedia.setShareMedia(uMusic);
		// circleMedia.setShareMedia(video);
		circleMedia.setTargetUrl(SharePath);
		mController.setShareMedia(circleMedia);

		// 设置renren分享内容
		RenrenShareContent renrenShareContent = new RenrenShareContent();
		renrenShareContent.setShareContent("人人分享内容");
		UMImage image = new UMImage(ActivityShare.this,
				BitmapFactory.decodeResource(getResources(),
						R.drawable.ic_launcher));
		image.setTitle("惠消费");
		image.setThumb("http://www.umeng.com/images/pic/social/integrated_3.png");
		renrenShareContent.setShareImage(image);
		renrenShareContent.setAppWebSite(SharePath);
		mController.setShareMedia(renrenShareContent);

		UMImage qzoneImage = new UMImage(ActivityShare.this,
				"http://www.umeng.com/images/pic/social/integrated_3.png");
		qzoneImage
				.setTargetUrl(SharePath);

		// 设置QQ空间分享内容
		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent("惠消费");
		qzone.setTargetUrl(SharePath);
		qzone.setTitle("惠消费");
		qzone.setShareMedia(localImage);
		// qzone.setShareMedia(uMusic);
		mController.setShareMedia(qzone);
		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent("惠消费");
		qqShareContent.setTitle("惠消费");
		qqShareContent.setShareMedia(image);
		qqShareContent.setTargetUrl(SharePath);
		mController.setShareMedia(qqShareContent);
		TencentWbShareContent tencent = new TencentWbShareContent();
		tencent.setShareContent("惠消费");
		// 设置tencent分享内容
		mController.setShareMedia(tencent);

		SinaShareContent sinaContent = new SinaShareContent();
		sinaContent
				.setShareContent("惠消费");
		sinaContent.setShareImage(new UMImage(ActivityShare.this,
				R.drawable.ic_launcher));
		mController.setShareMedia(sinaContent);
	}

	/**
	 * 配置分享平台参数</br>
	 */
	public void configPlatforms(String SharePath) {
		// 添加新浪SSO授权
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		// 添加腾讯微博SSO授权
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		// 添加人人网SSO授权
		RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(
				ActivityShare.this, "201874",
				"28401c0964f04a72a14c812d6132fcef",
				"3bf66e42db1e4fa9829b955cc300b737");
		mController.getConfig().setSsoHandler(renrenSsoHandler);

		// 添加QQ、QZone平台
		addQQQZonePlatform(SharePath);
		// 添加微信、微信朋友圈平台
		addWXPlatform();
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	public void addQQQZonePlatform(String SharePath) {
		String appId = "100424468";
		String appKey = "c7394704798a158208a74ab60104f0ba";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(ActivityShare.this,
				appId, appKey);
		qqSsoHandler.setTargetUrl(SharePath);
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
				ActivityShare.this, appId, appKey);
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	public void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx967daebe835fbeac";
		String appSecret = "5bb696d9ccd75a38c8a0bfe0675559b3";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(ActivityShare.this, appId,
				appSecret);
		wxHandler.addToSocialSDK();

		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(ActivityShare.this,
				appId, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		mController.getConfig().cleanListeners();
	}

}
