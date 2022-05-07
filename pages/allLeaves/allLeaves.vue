<template>
	<view class="handle-leave-page">
		<view class="leave-notes">
			<uni-notice-bar scrollable="true" single="true" text="为落实落细防疫工作,请各位同学在离校和返校后填写确认离校和返校信息。如未出行，也请在确认离校和确认返校界面中点取消行程。"></uni-notice-bar>
			<uni-segmented-control :values="current" active-color="#1b478e" style-type="text"></uni-segmented-control>
			<uni-card v-for="(item, index) in leaveNoteList" 
				:key="item.studentNumber" note="true">
				<view class="card-header">
					<uni-tag :text="item.type == 0 ? '未审核': item.type == 1 ? '已通过': '已拒绝' " :type="item.type == 0? 'default': item.type == 1 ? 'success': 'error'"></uni-tag>
					<text class="right">{{item.startTime}}</text>
				</view>
				<view><text>姓名: {{item.applicant}}</text></view>
				<view><text>班级: {{item.majorAndClass}}</text></view>
				<view><text>学号: {{item.studentNumber}}</text></view>
				<view><text>是否离校: {{item.leave}}</text></view>
				<view><text>请假天数: 3天</text></view>
				<view slot="actions" class="card-actions">
					<view class="card-actions-item" @click="checkDetails(item.studentNumber)">
						<text class="card-actions-item-text">查看详情</text>
						<uni-icons type="arrow-right" size="20" color="#1b478e"></uni-icons>
					</view>
				</view>
			</uni-card>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				current:['未处理','已审核'],
				leaveNoteList: [{
						noteNumber: null,
						studentNumber: 202026203039,
						applicant: "张三",
						majorAndClass: "20级计科1班",
						startTime: "2020-4-26 15:00:00",
						endTime: "2020-4-27 15:00:00",
						leave: "是",
						destination: "宿舍",
						dormitoryNumber: "6栋s121",
						way: "地铁",
						phoneNumber: "12832919291",
						instructorOpinion: "",
						insituteOpionion: "",
						type:1
					},
					{
						noteNumber: null,
						studentNumber: 202026203039,
						applicant: "张三",
						majorAndClass: "20级计科1班",
						startTime: "2020-4-26 15:00:00",
						endTime: "2020-4-27 15:00:00",
						leave: "是",
						destination: "宿舍",
						dormitoryNumber: "6栋s121",
						way: "地铁",
						phoneNumber: "12832919291",
						instructorOpinion: "",
						insituteOpionion: "",
						type:2
					},
					{
						noteNumber: null,
						studentNumber: 202026203039,
						applicant: "张三",
						majorAndClass: "20级计科1班",
						startTime: "2020-4-26 15:00:00",
						endTime: "2020-4-27 15:00:00",
						leave: "是",
						destination: "宿舍",
						dormitoryNumber: "6栋s121",
						way: "地铁",
						phoneNumber: "12832919291",
						instructorOpinion: "",
						insituteOpionion: "",
						type:0
					},
					{
						noteNumber: null,
						studentNumber: 202026203039,
						applicant: "张三",
						majorAndClass: "20级计科1班",
						startTime: "2020-4-26 15:00:00",
						endTime: "2020-4-27 15:00:00",
						leave: "是",
						destination: "宿舍",
						dormitoryNumber: "6栋s121",
						way: "地铁",
						phoneNumber: "12832919291",
						instructorOpinion: "",
						insituteOpionion: "",
						type:0
					},
					{
						noteNumber: null,
						studentNumber: 202026203039,
						applicant: "张三",
						majorAndClass: "20级计科1班",
						startTime: "2020-4-26 15:00:00",
						endTime: "2020-4-27 15:00:00",
						leave: "是",
						destination: "宿舍",
						dormitoryNumber: "6栋s121",
						way: "地铁",
						phoneNumber: "12832919291",
						instructorOpinion: "",
						insituteOpionion: "",
						type:0
					}
				]

			}
		},
		onLoad() {

		},
		methods: {
			checkDetails(id){
				uni.navigateTo({
					url: "../allLeaveDetails/allLeaveDetails?id=" + id,
					animationType: 'pop-in',
					animationDuration: 200
				})
			}
		}
	}
</script>

<style lang="scss">
	.handle-leave-page {
		margin: 0 10rpx;

		.card-actions-item {
			display: flex;
			justify-content: flex-end;
			align-items: center;
			color: $jxnu-bg-color;
		}
		.card-header{
			margin-bottom: 20rpx;
			.right{
				float: right;
				font-size:12px;
				color: $uni-text-color-grey;
			}
		}
	}
</style>
