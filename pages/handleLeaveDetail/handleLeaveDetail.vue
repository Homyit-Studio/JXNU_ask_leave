<template>
	<view class="handle-leave-details-page">
		<view class="detail-status">
			<uni-tag text="未处理" type="error" />
			<view class="current-time">
				<text class="time-tag">2020-12-22 20:11:11</text>
			</view>
		</view>
		<view class="details-card">
			<uni-card :title="leaveDetails.applicant + ' 的请假申请'">
				<view><text decode="true">学&emsp;&emsp;号:&emsp;{{leaveDetails.studentNumber}}</text></view>
				<view><text decode="true">班&emsp;&emsp;级:&emsp;{{leaveDetails.majorAndClass}}</text></view>
				<view><text decode="true">目的地点:&emsp;{{leaveDetails.destination}}</text></view>
				<view><text decode="true">是否离校:&emsp;{{leaveDetails.leave}}</text></view>
				<view>
					<view><text decode="true">开始时间:&emsp;{{leaveDetails.startTime}}</text></view>
					<view><text decode="true">结束时间:&emsp;{{leaveDetails.endTime}}</text></view>
					<uni-tag>4小时</uni-tag>
				</view>
				<view><text decode="true">前往方式:&emsp;{{leaveDetails.way}}</text></view>
				<view><text decode="true">联系号码:&emsp;{{leaveDetails.phoneNumber}}</text></view>
				<view><text decode="true">宿舍楼栋:&emsp;{{leaveDetails.dormitoryNumber}}</text></view>
			</uni-card>
		</view>
		<view class="approval-record">
			<view class="record">审批流程记录</view>
			<view>
				<uni-steps :options="[{title: '事件一'}, {title: '事件二'}, {title: '事件三'}]" :active="1" direction="column">
				</uni-steps>
			</view>
		</view>
		<view class="handle-buttons">
			<button type="warn" class="refuse-button" @click="cancelSubmit">拒绝</button>
			<button type="primary" class="agree-button" @click="reviseSubmit">同意</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				leaveDetails: {
					noteNumber: null,
					studentNumber: 202026203039,
					applicant: "张三",
					majorAndClass: "20级计科1班",
					startTime: "2020-4-26 15:00:00",
					endTime: "2020-4-27 15:00:00",
					leave: "是",
					destination: "宿舍",
					dormitoryNumber: "6栋 s121",
					way: "地铁",
					phoneNumber: "12832919291",
					instructorOpinion: "",
					insituteOpionion: "",
					type: 0
				}
			}
		},
		onLoad(id) {
			console.log(id)
		},
		methods: {}
	}
</script>

<style lang="scss">
	.handle-leave-details-page {
		background-color: #f8f8f8;

		.detail-status {
			text-align: center;
			background-color: #fff;
			color: #378d8c;
			padding: 10rpx 0;
		}

		.current-time {
			font-size: 12rpx;
			text-align: center;
			padding-top: 20rpx;

			.time-tag {
				padding: 2rpx;
				background-color: #999;
				border-radius: 10rpx;
				color: #fff;
			}
		}

		.details-card {
			.uni-card {
				margin-top: 0;
				padding-top: 0;
			}
		}

		.approval-record {
			background-color: #fff;

			padding: 20rpx 70rpx;
		}

		.handle-buttons {
			display: flex;
			justify-content: center;
			padding-bottom: 50rpx;

			button {
				width: 300rpx;
				height: 60rpx;
				line-height: 60rpx;
				font-size: $jxnu-font-14;
			}
			.agree-button{
				background-color: $jxnu-bg-color;
			}
		}
	}
</style>
