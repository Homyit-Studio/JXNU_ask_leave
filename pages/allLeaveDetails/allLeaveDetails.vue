<template>
		<view class="handle-leave-details-page">
		<view class="detail-status">
			<view  v-if = "types === '0'">
				<view>
					<uni-icons type="minus-filled" size="120" color="#7f7f7f" class="icon-style"></uni-icons>
				</view>
				<text>未审批</text>
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
			<view  v-else>
				<view>
					<uni-icons type="minus-filled" size="120" color="#ffaa00" class="icon-style"></uni-icons>
				</view>
				<text>审批中</text>
			</view>
		
		</view>
		<view class="details-card">
			<uni-card title="审批情况" :is-shadow="false"  class="details-card">
				<view><text decode="true">辅导员意见:&emsp;{{leaveDetails.instructorOpinion === null ? '暂无' : leaveDetails.instructorOpinion}}</text></view>
				<view><text decode="true">学院&emsp;意见:&emsp;{{leaveDetails.instituteOpinion === null ? '暂无' : leaveDetails.instituteOpinion}}</text></view>
				<view><text decode="true">院长&emsp;意见:&emsp;{{leaveDetails.deanOpinion === null ? '暂无' : leaveDetails.deanOpinion}}</text></view>
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
					<uni-tag>4小时</uni-tag>
				</view>
				<view><text decode="true">前往方式:&emsp;{{leaveDetails.way}}</text></view>
				<view><text decode="true">联系号码:&emsp;{{leaveDetails.phoneNumber}}</text></view>
				<view><text decode="true">宿舍楼栋:&emsp;{{leaveDetails.dormitoryNumber}}</text></view>
				<view>
					<text decode="true">请假事由:&emsp;</text>
					<text class="reason-text">{{leaveDetails.reason}}</text>
				</view>
			</uni-card>
			<view class="btn-grounps">
				<button type="default" @click="goToTerminate()">我要销假</button>
			</view>
		</view>
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
			//console.log(this.leaveDetails.examine)
			//console.log(this.types == '0')
		},
		methods: {
			goToTerminate(){
				uni.navigateTo({
					url:'/pages/terminateLeave/terminateLeave?id=' + this.leaveDetails.id
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
				background-color: $uni-bg-color;
				border: 1px solid $jxnu-bg-color; /* Green */
				color: $jxnu-bg-color;
				font-size: $jxnu-font-16;
			}
			
		}

	}
</style>
