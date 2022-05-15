<template>
	<view class="handle-leave-details-page">
		<view class="detail-status">
			<view  v-if = "types === '0'">
				<view>
					<uni-icons type="minus-filled" size="120" color="#ffaa00" class="icon-style"></uni-icons>
				</view>
				<text>审批中</text>
			</view>
			<view  v-else-if="leaveDetails.examine === 'SUCCESS'">
				<view>
					<uni-icons type="checkbox-filled" size="120" color="#00aa27" class="icon-style"></uni-icons>
				</view>
				<text>审批通过</text>
			</view>
			<view  v-else-if="leaveDetails.examine === 'FAILURE'">
				<view>
					<uni-icons type="clear" size="120" color="#aa0000" class="icon-style"></uni-icons>
				</view>
				<text>审批未通过</text>
			</view>	
		</view>
		<view class="details-card">
			<uni-card title="审批情况" :is-shadow="false"  class="details-card">
				<view>
					<text decode="true">辅导员意见:&emsp;</text>
					<text class="reason-text">{{leaveDetails.instructorOpinion === null ? '暂无' : leaveDetails.instructorOpinion}}</text>
				</view>
				
				<view>
					<text decode="true">学院&emsp;意见:&emsp;</text>
					<text class="reason-text">{{leaveDetails.secretaryOpinion === null ? '暂无' : leaveDetails.secretaryOpinion}}</text>
				</view>
				<view>
					<text decode="true">院长&emsp;意见:&emsp;</text>
					<text class="reason-text">{{leaveDetails.deanOpinion === null ? '暂无' : leaveDetails.deanOpinion}}</text>
				</view>
			</uni-card>
		</view>
		<view class="details-card">
			<uni-card title="请假详情" :extra="'申请时间'+ leaveDetails.startTime" :is-shadow="false" >
				<view><text decode="true">学&emsp;&emsp;号:&emsp;{{leaveDetails.studentNumber}}</text></view>
				<view><text decode="true">班&emsp;&emsp;级:&emsp;{{leaveDetails.majorAndClass}}</text></view>
				<view><text decode="true">目的地点:&emsp;{{leaveDetails.destination}}</text></view>
				<view><text decode="true">是否离校:&emsp;{{leaveDetails.depart === 'YES' ? '是' : leaveDetails.depart === 'NO' ? '否' : ''}}</text></view>
				<view>
					<view><text decode="true">开始时间:&emsp;{{leaveDetails.startTime}}</text></view>
					<view><text decode="true">结束时间:&emsp;{{leaveDetails.endTime}}</text></view>
				</view>
				<view><text decode="true">前往方式:&emsp;{{leaveDetails.way}}</text></view>
				<view><text decode="true">联系号码:&emsp;{{leaveDetails.phoneNumber}}</text></view>
				<view><text decode="true">宿舍楼栋:&emsp;{{leaveDetails.dormitoryNumber}}</text></view>
				<view>
					<text decode="true">请假事由:&emsp;</text>
					<text class="reason-text">{{leaveDetails.reason}}</text>
				</view>
			</uni-card>
			<view class="btn-grounps" v-if="leaveDetails.examine === 'SUCCESS'">
				<button type="default" class="withOutBoder" @click="checkTerminate()" v-if="leaveDetails.status ==='SUCCESS'">查看销假</button>
				<button type="default" class="withOutBoder" @click="goToTerminate()" v-else>我要销假</button>
			</view>
			<view class="btn-grounps" v-else>
				<button type="default" class="deteleWarn" @click="deteleLeave()">删除假条</button>
			</view>
			<view class="btn-grounps">
				<button type="default" class="deteleWarn" @click="deteleLeave()">添加附件</button>
			</view>
		</view>
		<!-- 确认删除弹框 -->
		<uni-popup ref="dialog_up" type="dialog">
			<uni-popup-dialog type="error" content="确认删除？" :duration="2000" :before-close="true" @close="close" @confirm="confirm"></uni-popup-dialog>
		</uni-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				types:'',
				leaveDetails: {
				}
			}
		},
		onLoad(item) {
			//console.log(item.type)
			//console.log(this.leaveDetails.type == '0')
			uni.$http.get('/leave/selectANote/' + item.id).then(res =>{
				console.log(res)
				if(res.data.code === 200){
					this.leaveDetails = res.data.data
				}
				
			})
			this.types = item.type;
			console.log(this.leaveDetails)
			
			//console.log(this.leaveDetails.examine)
			//console.log(this.types == '0')
		},
		methods: {
			goToTerminate(){
				uni.navigateTo({
					url:'/pages/terminateLeave/terminateLeave?id=' + this.leaveDetails.id
				})
			},
			//查看销假
			checkTerminate(){
				uni.navigateTo({
					url:'/pages/checkTerminateLeave/checkTerminateLeave?id=' + this.leaveDetails.id
				})
			},
			//删除假条
			deteleLeave(){
				this.$refs.dialog_up.open()
			},
			//弹框操作
			close() {
				this.$refs.dialog_up.close()
			},
			//确认删除
			confirm() {
				console.log(this.leaveDetails.id)
				uni.$http.get('/leave/deletedANote/' + this.leaveDetails.id).then(res =>{
					console.log(res)
					if(res.data.code === 200){
						uni.showToast({
							title: "删除成功",		
						})
						setTimeout(function() {
							uni.redirectTo({
								url:'/pages/allLeaves/allLeaves'
								} 
							)
						}, 1000)
					}
				}).catch(err=>{
					console.log(err)
				})			
		}}
		}
</script>

<style lang="scss">
	.handle-leave-details-page {
		.detail-status {
			text-align: center;
			background-color: #fff;
			color: $uni-text-color;
			padding: 10rpx 0;
		}

		.details-card {
			.uni-card {
				margin-top: 0;
				padding-top: 0;
				text{
					color: $uni-text-color;
					font-size: 13px;
				}
				.reason-text{
					display: inline-block;
					width: 400rpx;
				}
			}
		}
		.btn-grounps{
			width: 700rpx;
			margin: 0rpx auto;
			button{
				height: 80rpx;
				font-size: $jxnu-font-16;
			}
			
		}
		.withOutBoder{
			background-color: $uni-bg-color;
			border: 1px solid $jxnu-bg-color;
			color: $jxnu-bg-color;
		}
		.deteleWarn{
			background-color: $uni-bg-color;
			border: 1px solid $uni-color-error;
			color: $uni-color-error;
		}

	}
</style>
