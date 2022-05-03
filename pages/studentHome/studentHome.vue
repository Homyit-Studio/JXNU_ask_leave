<template>
	<view class="student-home">
		<view class="backgrond-style"></view>
		<view>
			<uni-card class="uni-card">
			    <view class="welcome-box">
					<text>欢迎，{{studentMsg.id}}</text>
					<uni-icons type="forward" size="20" color="#585858" class="more-icon" @click="showStudentMsg()"></uni-icons>
			    </view>
			</uni-card>
		</view>
		<view>
			<uni-card class="uni-card">
				<uni-row>
					   <text>常用工具</text>
				</uni-row>
			    <uni-row class="btn-box">
			    		<view class="btn-style">
							<navigator url="/pages/applyLeave/applyLeave" animation-type="pop-in" animation-duration="300">
								<view><uni-icons type="contact" size="35" color="#f0f0f0" class="icon-style" ></uni-icons></view>
							</navigator>
							<text>请假申请</text>
						</view>
						<view class="btn-style">
							<navigator url="/pages/allLeaves/allLeaves" animation-type="pop-in" animation-duration="300">
								<view><uni-icons type="contact" size="35" color="#f0f0f0" class="icon-style"></uni-icons></view>
							</navigator>
							<text>申请记录</text>
						</view>
						<view class="btn-style">
							<view><uni-icons type="contact" size="35" color="#f0f0f0" class="icon-style"></uni-icons></view>
							<text>退出登录</text>
						</view>
			    </uni-row>
			</uni-card>
		</view>
		<uni-popup ref="popup" type="bottom">
			<uni-list class="list-style">
				<uni-list-item  title="学号" :rightText="studentMsg.id" ></uni-list-item>
				<uni-list-item  title="姓名" :rightText="studentMsg.name" ></uni-list-item>
				<uni-list-item  title="班级" :rightText="studentMsg.class"></uni-list-item>
				<uni-list-item  title="辅导员" :rightText="studentMsg.instructor"></uni-list-item>
			</uni-list>
		</uni-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				studentMsg:{
					id:"202026202156",
					name:'薛二狗',
					class:'26级计算机科学与技术5班',
					instructor:'樊砂砾'
				}
			}
		},
		methods:{
			showStudentMsg(){
				//弹出学生信息页面
				this.$refs.popup.open()
			}
		}
	}
</script>

<style lang="scss">
	@mixin bgstyle($bgstyle: #1b478e) {
		width: 100vw;
		height: 310rpx;
		position: absolute;
		top: -100rpx;
		border-radius: 0 0 40rpx 40rpx;
		background: $bgstyle
	}
	
	.backgrond-style {
		@include bgstyle;
	}
	
	.student-home{
		margin: 0 auto;
		.uni-card{
			border-radius: 20rpx;
			text{
				font-size: $jxnu-font-14;
			}
			.welcome-box{
				height: 200rpx;
				text{
					line-height: 200rpx;
					font-size: $jxnu-font-18;
				}
			}
			.btn-box{
				height: 220rpx;
				margin-top: 10rpx;
			}
			.btn-style{
				display: inline-block;
				margin-right: 50rpx;
				width: 100rpx;
				height: 100rpx;
				background-color: $jxnu-bg-color;
				border-radius: 30rpx;
				text-align: center;
				color: $uni-text-color;
				text{
					font-size: 13rpx;
				}
			}
			.icon-style{
				line-height: 100rpx;
			}
			.more-icon{
				float: right;
			}
		}
		.list-style{
			height: 600rpx;
			margin: 0 auto;
			uni-view{
				height: 100rpx;
			}
		}
	}	
</style>
