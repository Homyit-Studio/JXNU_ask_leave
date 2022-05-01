<template>
	<view class="student-home">
		<view class="backgrond-style"></view>
		<uni-card class="uni-card">
		    <view class="welcome-box">
				<text>欢迎，XXXX</text>
		    </view>
		</uni-card>
		<uni-card class="uni-card">
			<uni-row>
				   常用工具
			</uni-row>
		    <uni-row class="btn-box">
		    	<uni-col :xs="6" :sm="5" :md="3" :lg="2" :xl="1">
		    		<view class="btn-style">
						<navigator url="/pages/applyLeave/applyLeave">
							<view><uni-icons type="contact" size="35" color="#f0f0f0" class="icon-style" ></uni-icons></view>
						</navigator>
						<text>请假申请</text>
					</view>
		    	</uni-col>
				<uni-col :xs="6" :sm="5" :md="3" :lg="2" :xl="1">
					<view class="btn-style">
						<view><uni-icons type="contact" size="35" color="#f0f0f0" class="icon-style"></uni-icons></view>
						<text>申请记录</text>
					</view>
				</uni-col>
				<uni-col :xs="6" :sm="6" :md="4" :lg="3" :xl="1">
					<view class="btn-style">
						<view><uni-icons type="contact" size="35" color="#f0f0f0" class="icon-style"></uni-icons></view>
						<text>退出登录</text>
					</view>
				</uni-col>
		    </uni-row>
		</uni-card>
	</view>
</template>

<script>
	export default {
		setup() {
			return {
				
			}
		},
	}
</script>

<style>
	.uni-card{
		border-radius: 20upx;
	}
	.welcome-box{
		height: 13vh;
	}
	.welcome-box text{
		line-height: 13vh;
		font-size: 35upx;
	}
	.btn-box{
		height: 15vh;
		margin-top: 20upx;
	}
	.btn-style{
		width: 100upx;
		height: 100upx;
		background-color:indianred;
		border-radius: 30upx;
		text-align: center;
		color: #4f4f4f;
		font-size: 8upx;
	}
	.icon-style{
		line-height: 100upx;
	}
	.backgrond-style{
		width: 100vw;
		height: 25vh;
		position: absolute;
		top: -11vh;
		border-radius: 0 0 40upx 40upx;
		background-color: indianred;
	}
</style>
