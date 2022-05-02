<template>
	<view>
			<view class="uni-padding-wrap uni-common-mt">
				<uni-segmented-control :current="currentIndex" :values="handleChoices" style-type="text"
					active-color="#1b478e" @clickItem="onClickChoice" />
			</view>
			<view class="content">
				r
			</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				currentIndex: 0,
				handleChoices:["未处理", "已处理"],
				leaveNoteList:[
					{
						noteNumber :null,
						studentNumber: 202026203039,
						applicant:"张三",
						majorAndClass:"20级计科1班",
						startTime:"2020-4-26 15:00:00",
						endTime: "2020-4-27 15:00:00",
						leave: "是",
						destination:"宿舍",
						dormitoryNumber:"6栋s121",
						way:"地铁",
						phoneNumber:"12832919291",
						instructorOpinion:"",
						insituteOpionion:""
					},
					{
						noteNumber :null,
						studentNumber: 202026203039,
						applicant:"张三",
						majorAndClass:"20级计科1班",
						startTime:"2020-4-26 15:00:00",
						endTime: "2020-4-27 15:00:00",
						leave: "是",
						destination:"宿舍",
						dormitoryNumber:"6栋s121",
						way:"地铁",
						phoneNumber:"12832919291",
						instructorOpinion:"",
						insituteOpionion:""
					},
					{
						noteNumber :null,
						studentNumber: 202026203039,
						applicant:"张三",
						majorAndClass:"20级计科1班",
						startTime:"2020-4-26 15:00:00",
						endTime: "2020-4-27 15:00:00",
						leave: "是",
						destination:"宿舍",
						dormitoryNumber:"6栋s121",
						way:"地铁",
						phoneNumber:"12832919291",
						instructorOpinion:"",
						insituteOpionion:""
					},
					{
						noteNumber :null,
						studentNumber: 202026203039,
						applicant:"张三",
						majorAndClass:"20级计科1班",
						startTime:"2020-4-26 15:00:00",
						endTime: "2020-4-27 15:00:00",
						leave: "是",
						destination:"宿舍",
						dormitoryNumber:"6栋s121",
						way:"地铁",
						phoneNumber:"12832919291",
						instructorOpinion:"",
						insituteOpionion:""
					},
					{
						noteNumber :null,
						studentNumber: 202026203039,
						applicant:"张三",
						majorAndClass:"20级计科1班",
						startTime:"2020-4-26 15:00:00",
						endTime: "2020-4-27 15:00:00",
						leave: "是",
						destination:"宿舍",
						dormitoryNumber:"6栋s121",
						way:"地铁",
						phoneNumber:"12832919291",
						instructorOpinion:"",
						insituteOpionion:""
					}
				]
				
			}
		},
		onLoad() {
	
		},
		methods: {
			onClickChoice(index){
				console.log(index)
			}
		}
	}
</script>

<style lang="scss">
	.uni-list-item {
		display: flex;
		flex-direction: row;
	}
</style>
