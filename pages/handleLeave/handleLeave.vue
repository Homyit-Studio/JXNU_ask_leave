<template>
	<view class="handle-leave-page">
		<view class="uni-padding-wrap uni-common-mt">
			<uni-segmented-control :current="currentIndex" :values="handleChoices" style-type="text"
				active-color="#1b478e" @clickItem="onClickChoice" />
		</view>
		<view class="leave-notes">
			<uni-card v-for="(item, index) in leaveNoteList" :title="currentTitle" :extra="item.startTime"
				:key="item.studentNumber">
				<view><text decode="true">姓&emsp;&emsp;名: {{item.applicant}}</text></view>
				<view><text decode="true">班&emsp;&emsp;级: {{item.majorAndClass}}</text></view>
				<view><text decode="true">学&emsp;&emsp;号: {{item.studentNumber}}</text></view>
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
				currentIndex: 0,
				currentTitle: "未处理假条",
				handleChoices: ["未处理", "已处理"],
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
						insituteOpionion: ""
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
						insituteOpionion: ""
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
						insituteOpionion: ""
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
						insituteOpionion: ""
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
						insituteOpionion: ""
					}
				]

			}
		},
		//不知为啥不生效
		// computed:{
		// 	currentTitle:{
		// 		get(){
		// 			console.log(4)
		// 			return this.currentIndex == 0? "3" : "4"
		// 		},
		// 		set(newValue){
		// 			console.log(newValue)
		// 		}
		// 	}
		// },
		onLoad() {
		},
		methods: {
			onClickChoice(index) {
				if (index.currentIndex == 0) {
					this.currentTitle = "未处理假条"
				} else {
					this.currentTitle = "已处理假条"
				}
			},
			checkDetails(studentId) {
				uni.navigateTo({
					url: "../handleLeaveDetail/handleLeaveDetail?id=" + studentId,
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
	}
</style>
