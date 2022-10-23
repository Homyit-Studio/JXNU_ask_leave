<template>
	<view class="search-leaves-page">
		<button type="button" class="search-button" @click="searchPopup">
			<uni-icons type="search"></uni-icons>查询
		</button>
		<view>
			<uni-list class="leave-list">
				<template v-for="(item, index) in leaveNoteList">
					<uni-list-item class="leave-list-item" direction="column">
						<template v-slot:header>
							<view class="card-header">
								<uni-tag v-if="item.examine == 'FAILURE'" :mark="true" :text="index+1+ '.'"
									type="error">
								</uni-tag>
								<uni-tag :mark="true" v-else :text="index+1 + '.'" type="default"></uni-tag>
								<view>
									<text>请假开始时间:{{item.startTime}}</text>
								</view>
							</view>
						</template>
						<template v-slot:body>
							<view><text decode="true">姓名: {{item.username}}</text></view>
							<view><text decode="true">班级: {{item.majorAndClass}}</text></view>
							<view><text decode="true">学号: {{item.studentNumber}}</text></view>
							<view><text>是否离校: {{item.depart == 'YES'? '是' :'否'}}</text></view>
							<view><text>请假时长: {{item.days}}</text></view>
						</template>
						<template v-slot:footer>
							<view class="card-actions">
								<view class="card-actions-item"
									@click="checkDetails(item.id)">
									<view class="tag-view">
										<uni-tag text="查看详情"
											custom-style="background-color: #1b478e; border-color: #1b478e; color: #fff;" />
									</view>
								</view>
							</view>
						</template>
					</uni-list-item>
				</template>
			</uni-list>
			<view class="show-nodata" v-if="shownodata"><text>没有更多数据了</text></view>
		</view>

		<view>
			<!-- 普通弹窗 -->
			<uni-popup ref="searchMessagePopup" background-color="#fff" type="center">
				<view class="popup-content">
					<uni-forms ref="form" :modelValue="searchForm">
						<uni-forms-item label="姓名" name="username">
							<uni-easyinput type="text" v-model="searchForm.username" placeholder="请输入学生姓名" />
						</uni-forms-item>
						<uni-forms-item label="学号" name="studentNumber">
							<uni-easyinput type="text" v-model="searchForm.studentNumber" placeholder="请输入学生学号" />
						</uni-forms-item>
						<uni-forms-item label="开始时间" name="startTime">
							<uni-datetime-picker type="datetime" v-model="searchForm.startTime" :border="false"
								:clear-icon="false" placeholder="请输入查找的开始时间" />
						</uni-forms-item>
						<uni-forms-item label="结束时间" name="endTime">
							<uni-datetime-picker type="datetime" v-model="searchForm.endTime" :border="false"
								:clear-icon="false" placeholder="请输入查找的结束时间" />
						</uni-forms-item>
					</uni-forms>
					<button @click="searchConfirm">开始查询</button>
				</view>
			</uni-popup>
		</view>
	</view>

</template>

<script>
	export default {
		data() {
			return {
				searchForm: {
					pageNo: 1,
					pageSize: 5,
					studentNumber: "",
					username: "",
					startTime: "",
					endTime: ""
				},
				isloading: false,
				leaveNoteList:[],
				shownodata: false,
				endPage:1
			};
		},
		onLoad(){
			// this.searchConfirm()
			// this.searchForm.startTime = this.searchForm.endTime = this.getFormatDate()
		},
		onShow(){
			this.searchForm.pageNo = 1
			this.searchConfirm()
		},
		methods: {
			searchPopup() {
				
				this.$refs.searchMessagePopup.open()
			},
			//获取当前格式化时间
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
			// 查找假条
			searchConfirm(){
				uni.showLoading({
					title:"查找中"
				})
				uni.$http.post("/leave/selectNotesByStudentInfo", this.searchForm).then(res => {
					if (res.data.code == 200) {
						this.leaveNoteList = res.data.data.list
						this.endPage = res.data.data.endPage
						console.log(this.endPage)
						uni.showToast({
							title:"查找成功"
						})
						this.$refs.searchMessagePopup.close()
						if (this.searchForm.pageNo >= this.endPage) {
							this.shownodata = true
						}
					} else {
						this.shownodata = true
						uni.showToast({
							icon:"error",
							title:res.data.message
						})
					}
				})
			},
			checkDetails(id){
				uni.navigateTo({
					url: `../modifyLeaveDetails/modifyLeaveDetails?id=${id}`,
					animationType: 'pop-in',
					animationDuration: 200
				})
			}
			
		},
		onReachBottom() {
			if (this.searchForm.pageNo >= this.endPage) {
				this.shownodata = true
				return
			}
			if (this.isloading) return;
			this.isloading = true
			this.searchForm.pageNo++;
			uni.$http.post(`/leave/selectNotesByStudentInfo`, this.searchForm).then(res => {
				if (res.data.code == 200) {
					this.leaveNoteList = [...this.leaveNoteList, ...res.data.data.list]
					this.isloading = false
				} else {
					this.listRequest.pageNo--;
					uni.showToast({
						title:"操作频繁，请重试",
						icon:"none"
					})
					this.isloading = false
				}
			})
		},
	}
</script>

<style lang="scss">
	.search-leaves-page {

		.search-button {
			margin: 10px;
			border-radius: 5rpx;
			height: 70rpx;
			line-height: 70rpx;
			background-color: #e4e4e4;
			font-size: 14px;

			.uni-icons {
				margin: 10px;
			}
		}
		.leave-list {
			padding: 30rpx;
				margin: 20rpx;
		
			.card-header {
				font-size: 12px;
			}
		
			text {
				font-size: 12px;
			}
			.card-actions-item {
				display: flex;
				justify-content: flex-end;
				align-items: center;
				color: $jxnu-bg-color;
				font-size: 12px;
			}
		}
		
		.show-nodata {
			text-align: center;
			padding: 20px;
		}
		
		
		

		
		
		.popup-content{
			padding: 30rpx;
	
			border-radius: 50rpx;
			
			button{
				height: 65rpx;
				width: 200rpx;
				font-size: 14px;
				background-color: $jxnu-bg-color;
				line-height: 65rpx;
				color: #fff;
			}
		}
	}
</style>
