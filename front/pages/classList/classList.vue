<template>
	<view class="class-list-page">
		<view class="grade-select" v-if="enterChoose == 1">
			<uni-data-select v-model="gradevalue" :localdata="gradeSelect" @change="changeGrade" label="年级">
			</uni-data-select>
			<p class="all-class">共有{{total}}个班</p>
		</view>
		<uni-card v-for="(item, i) in classList" :key="item.id" ellipsis="{{2}}" :title="item.majorAndClass"
			:thumbnail="avatar" :sub-title="'id号：' + item.id" :extra="'班级人数：' + item.capacity">
			<view slot="actions" class="card-actions">
				<view class="card-actions-item" @click="lookRoster(item.id, item.majorAndClass,item.gradeId)">
					<text class="card-actions-item-text">班级成员名单</text>
					<uni-icons type="arrow-right" size="18" color="#999"></uni-icons>
				</view>
			</view>
		</uni-card>
		<view>
			<!-- 提示信息弹窗 -->
			<uni-popup ref="message" type="message">
				<uni-popup-message :type="msg.msgType" :message="msg.messageText" :duration="2000"></uni-popup-message>
			</uni-popup>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				avatar: "https://bkimg.cdn.bcebos.com/pic/b21c8701a18b87d6473830f5000828381e30fde6?x-bce-process=image/watermark,image_d2F0ZXIvYmFpa2UyNzI=,g_7,xp_5,yp_5/format,f_auto",
				classList: [],
				enterChoose: null,
				msg: {
					msgType: 'success',
					messageText: '这是一条成功提示',
				},
				total:0,
				gradevalue: "",
				gradeSelect: [{
						value: "",
						text: "大一"
					},
					{
						value: "",
						text: "大二"
					},
					{
						value: "",
						text: "大三"
					},
					{
						value: "",
						text: "大四"
					},
					{
						value: "",
						text: "研一"
					},
					{
						value: "",
						text: "研二"
					},
					{
						value: "",
						text: "研三"
					},
					{
						value: "1018",
						text: "留学生四年级"
					},
					{
						value: "1019",
						text: "留学生三年级"
					}
				],
			}
		},
		onLoad(options) {
			this.enterChoose = options.choose
			if(this.enterChoose == 1){
				this.setGradeYear()
			}
			this.requestClassList()
		},
		methods: {
			// 设置年级年份
			setGradeYear() {
				let date = new Date()
				let year = date.getFullYear()
				let month = date.getMonth()
				let newsign = month > 8 ? true : false
				this.gradevalue = newsign ? JSON.stringify(year) : JSON.stringify(year - 1)
				this.gradeSelect.map(item => {
					switch (item.text) {
						case "大一":
							item.value = newsign ? JSON.stringify(year) : JSON.stringify(year - 1)
							break;
						case "大二":
							item.value = newsign ? JSON.stringify(year - 1) : JSON.stringify(year - 2)
							break;
						case "大三":
							item.value = newsign ? JSON.stringify(year - 2) : JSON.stringify(year - 3)
							break;
						case "大四":
							item.value = newsign ? JSON.stringify(year - 3) : JSON.stringify(year - 4)
							break;
						case "研一":
							item.value = newsign ? JSON.stringify(year + 1000) : JSON.stringify(year + 999)
							break;
						case "研二":
							item.value = newsign ? JSON.stringify(year + 999) : JSON.stringify(year + 998)
							break;
						case "研三":
							item.value = newsign ? JSON.stringify(year + 998) : JSON.stringify(year + 997)
							break;
					}
				})
			},
			changeGrade(grade) {
				// v-model没有用
				this.gradevalue = grade
				this.requestClassList()
			
			},
			lookRoster(id, className, gradeId) {
				uni.navigateTo({
					url: `../classRoster/classRoster?id=${id}&class=${className}&choose=${this.enterChoose}&gradeId=${gradeId}`,
					animationType: 'pop-in',
					animationDuration: 200
				})
			},
			requestClassList() {
				if (this.enterChoose == 0) {
					uni.$http.get("/teacher/getClassInfo").then(res => {
						if (res.data.code == 200) {
							uni.showToast({
								title: '加载中',
								duration: 500,
								icon: "loading"
							});
							this.classList = res.data.data
						} else {
							this.msg.msgType = "error"
							this.msg.messageText = "请求错误"
							this.$refs.message.open()
						}
					})
				} else if (this.enterChoose == 1) {
					console.log(this.gradevalue)
					uni.$http.get(`/teacher/getAllClass/${this.gradevalue}`).then(res => {
						console.log(res)
						if (res.data.code == 200) {
							uni.showToast({
								title: '加载中',
								duration: 500,
								icon: "loading"
							});
							const {list, total} = res.data.data
							this.classList = list
							this.total = total
						} else {
							this.msg.msgType = "error"
							this.msg.messageText = "请求错误"
							this.$refs.message.open()
						}
					})
				}
			},
			
		}
	}
</script>

<style lang="scss">
	.class-list-page {
		.card-actions-item {
			display: flex;
			justify-content: flex-end;
		}
		.all-class{
			text-align: center;
		}
	}
</style>
