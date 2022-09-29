<template>
	<view class="class-list-page">
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
			}
		},
		onLoad(options) {
			this.enterChoose = options.choose
			this.requestClassList()
		},
		methods: {
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
					uni.$http.get("/teacher/getAllClass").then(res => {
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
	}
</style>
