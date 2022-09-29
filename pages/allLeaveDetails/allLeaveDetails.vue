<template>
	<view class="handle-leave-details-page">
		<view class="detail-status">
			<view v-if="types === 'PROCESSING'">
				<view>
					<uni-icons type="minus-filled" size="120" color="#ffaa00" class="icon-style"></uni-icons>
				</view>
				<text>审批中</text>
			</view>
			<view v-else-if="types === 'PROCESSED'">
				<view>
					<uni-icons type="checkbox-filled" size="120" color="#1b478e" class="icon-style"></uni-icons>
				</view>
				<text>销假完成</text>
			</view>
			<view v-else-if="types === 'WAIT_REPORT' || isextendTime">
				<view>
					<uni-icons type="minus-filled" size="120" color="#1b478e" class="icon-style"></uni-icons>
				</view>
				<text>等待销假</text>
			</view>
			<view v-else-if="types === 'REPORT_EXPIRED' && !isextendTime">
				<view>
					<uni-icons type="minus-filled" size="120" color="#77787f" class="icon-style"></uni-icons>
				</view>
				<text>销假过期</text>
			</view>
			<view v-else-if="types === 'APPLY_EXPIRED'">
				<view>
					<uni-icons type="minus-filled" size="120" color="#77787f" class="icon-style"></uni-icons>
				</view>
				<text>申请过期</text>
			</view>
			<view v-else-if="types === 'FAILURE'">
				<view>
					<uni-icons type="clear" size="120" color="#aa0000" class="icon-style"></uni-icons>
				</view>
				<text>已拒绝</text>
			</view>
		</view>
		<view class="details-card">
			<uni-card title="审批情况" :is-shadow="false" class="details-card">
				<view>
					<text
						decode="true">辅导员意见:&emsp;{{leaveDetails.instructorOpinion === null ? '暂无' : leaveDetails.instructorOpinion}}</text>
				</view>
				<view>
					<text
						decode="true">学院&emsp;意见:&emsp;{{leaveDetails.secretaryOpinion === null ? '暂无' : leaveDetails.secretaryOpinion}}</text>
				</view>
				<view>
					<text
						decode="true">院长&emsp;意见:&emsp;{{leaveDetails.deanOpinion === null ? '暂无' : leaveDetails.deanOpinion}}</text>
				</view>
			</uni-card>
		</view>
		<view class="details-card">
			<uni-card title="请假详情" :is-shadow="false">
				<view><text decode="true">学&emsp;&emsp;号:&emsp;{{leaveDetails.studentNumber}}</text></view>
				<view><text decode="true">班&emsp;&emsp;级:&emsp;{{leaveDetails.majorAndClass}}</text></view>
				<view><text decode="true">目的地点:&emsp;{{leaveDetails.destination}}</text></view>
				<view><text
						decode="true">是否离校:&emsp;{{leaveDetails.depart === 'YES' ? '是' : leaveDetails.depart === 'NO' ? '否' : ''}}</text>
				</view>
				<view>
					<view><text decode="true">开始时间:&emsp;{{leaveDetails.startTime}}</text></view>
					<view><text decode="true">结束时间:&emsp;{{leaveDetails.endTime}}</text></view>
				</view>
				<view><text decode="true">前往方式:&emsp;{{leaveDetails.way}}</text></view>
				<view><text decode="true">联系号码:&emsp;{{leaveDetails.phoneNumber}}</text></view>
				<view><text decode="true">宿舍楼栋:&emsp;{{leaveDetails.dormitoryNumber}}</text></view>
				<view>
					<text decode="true">请假事由:&emsp;{{leaveDetails.reason}}</text>
				</view>
				<view>
					<text decode="true">附&emsp;&emsp;件:&emsp;</text>
					<view>
						<image v-for="item in imgs" :key="item.id" :src="item.url" mode="aspectFill"
							@click="preview(item)"></image>
					</view>
				</view>
			</uni-card>
			<!-- 根据不同状态展示不同按钮 -->
			<view class="bottom-button">
				<!-- 等待处理 -->
				<view class="btn-grounps" v-if="types === 'PROCESSING'">
					<button size="mini" type="default" class="Boder" @click="addAttachment()">{{this.btn_text}}</button>
					<button size="mini" type="default" class="deteleWarn" @click="deteleLeave()">删除假条</button>
				</view>
				<!-- 已销假 -->
				<view class="btn-grounps" v-else-if="types === 'PROCESSED'">
					<button size="mini" type="default" class="withOutBoder" @click="checkTerminate()">查看销假</button>
					<!-- <button type="default" class="withOutBoder" @click="goToTerminate()" v-else>我要销假</button> -->
				</view>
				<!-- 等待销假 -->
				<view class="btn-grounps" v-else-if="types === 'WAIT_REPORT' || isextendTime">
					<button size="mini" type="default" class="withOutBoder" @click="goToTerminate()">我要销假</button>
				</view>
				<!-- 销假过期 -->
				<!-- 申请过期 -->
				<!-- 被拒假条 -->
				<!-- <view class="btn-grounps" v-else>
					<button size="mini" type="default" class="deteleWarn" @click="deteleLeave()">删除假条</button>
				</view>	 -->
			</view>
		</view>
		<!-- 确认删除弹框 -->
		<uni-popup ref="dialog_up" type="dialog">
			<uni-popup-dialog type="error" content="确认删除？" :duration="2000" :before-close="true" @close="close"
				@confirm="confirm"></uni-popup-dialog>
		</uni-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				btn_text: "添加附件",
				types: '',
				leaveDetails: {},
				imgs: [],
				baseUrl: "https://leave.jxnu.edu.cn",
				isextendTime: true
			}
		},
		onLoad(item) {
			uni.showLoading({
				title: '加载中',
				duration: 500,
			})
			uni.$http.get('/leave/selectANote/' + item.id).then(res => {
				if (res.data.code === 200) {
					this.leaveDetails = res.data.data
					let currentTime = this.getFormatDate()
					let days = parseInt((Date.parse(new Date(currentTime)) - Date.parse(new Date(this.leaveDetails.endTime)))/ (1000*3600*24))
					if(days > 3){
						this.isextendTime = false
					}
					uni.hideLoading();
					//le.log(this.leaveDetails)
				}

			})
			this.types = item.type;
			//获取假条图片
			uni.$http.get('/image/' + item.id).then(res => {
				if (res.data.code === 200) {
					this.imgs = res.data.data
					for (let item in this.imgs) {
						this.imgs[item].url = this.baseUrl + this.imgs[item].url;
						//console.log(this.imgs[item])
					}
					//有图片，替换附件
					if (this.imgs.length) {
						this.btn_text = "替换附件"
					}
				}

			})

		},
		methods: {
			getFormatDate() {
				var date = new Date();
				var sign1 = "-";
				var sign2 = ":";
				var year = date.getFullYear() // 年
				var month = date.getMonth() + 1; // 月
				var day = date.getDate(); // 日
				var hour = date.getHours(); // 时
				var minutes = date.getMinutes(); // 分
				var seconds = date.getSeconds() //秒
				// 给一位数数据前面加 “0”
				if (month >= 1 && month <= 9) {
					month = "0" + month;
				}
				if (day >= 0 && day <= 9) {
					day = "0" + day;
				}
				if (hour >= 0 && hour <= 9) {
					hour = "0" + hour;
				}
				if (minutes >= 0 && minutes <= 9) {
					minutes = "0" + minutes;
				}
				if (seconds >= 0 && seconds <= 9) {
					seconds = "0" + seconds;
				}
				var currentdate = year + sign1 + month + sign1 + day + " " + hour + sign2 + minutes + sign2 + seconds;
				return currentdate;
			},
			//添加附件
			addAttachment() {
				uni.redirectTo({
					url: '/pages/addAttachment/addAttachment?id=' + this.leaveDetails.id + '&type=' + this.types,
				})
			},
			//去销假
			goToTerminate() {
				uni.navigateTo({
					url: '/pages/terminateLeave/terminateLeave?id=' + this.leaveDetails.id
				})
			},
			//查看销假
			checkTerminate() {
				uni.navigateTo({
					url: '/pages/checkTerminateLeave/checkTerminateLeave?id=' + this.leaveDetails.id
				})
			},
			//删除假条
			deteleLeave() {
				this.$refs.dialog_up.open()
			},
			//弹框操作
			close() {
				this.$refs.dialog_up.close()
			},
			//图片预览
			preview(e) {
				//le.log(e)
				let array = [];
				array.push(e.url);
				uni.previewImage({
					current: array[0],
					urls: array
				});
			},
			//确认删除
			confirm() {
				uni.$http.get('/leave/deletedANote/' + this.leaveDetails.id).then(res => {
					//le.log(res)
					if (res.data.code === 200) {
						uni.showToast({
							title: "删除成功",
						})
						setTimeout(function() {
							uni.navigateBack({})
						}, 1000)
					} else {
						uni.showToast({
							icon: "none",
							title: res.data.message,
						})
					}
				}).catch(err => {
					uni.showToast({
						title: "网络似乎出现了一些问题",
					})
				})
			}
		}
	}
</script>

<style lang="scss">
	.handle-leave-details-page {
		.detail-status {
			text-align: center;
			background-color: #fff;
			color: $uni-text-color;
			padding: 10px 0;
		}

		.details-card {
			.uni-card {
				margin-top: 0;
				padding-top: 0;

				text {
					color: $uni-text-color;
					font-size: 13px;
				}

				.reason-text {
					display: inline-block;
					width: 400px;
				}

				image {
					width: 80px;
					height: 80px;
				}
			}
		}

		.bottom-button {
			margin-bottom: 20px;
		}

		.btn-grounps {
			// width: 700rpx;
			text-align: center;

			// margin: 0px auto;
			button {
				width: 450rpx;
				margin-top: 5px;
				// height: 50px;
				// line-height: 50px;
				// font-size: $jxnu-font-16;
			}

			.withOutBoder {
				background-color: $uni-bg-color;
				border: 1px solid $jxnu-bg-color;
				color: $jxnu-bg-color;
			}

			.deteleWarn {
				background-color: $uni-bg-color;
				border: 1px solid $uni-color-error;
				color: $uni-color-error;
			}

			.Boder {
				background-color: $jxnu-bg-color;
				// border: 1px solid $uni-color-error;
				color: $uni-bg-color;
			}

		}

	}
</style>
