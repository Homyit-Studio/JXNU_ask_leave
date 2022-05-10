<template>
	<view class="handle-leave-page">
		<view class="leave-notes">
			<uni-notice-bar scrollable="true" single="true" text="为落实落细防疫工作,请各位同学在离校和返校后进行假条销假。如未出行，也请在假条销假界面中取消行程。" showIcon></uni-notice-bar>
			<uni-segmented-control :values="current" :current="currentIndex"  @clickItem="onClickItem" active-color="#1b478e" style-type="text"></uni-segmented-control>
			<view>
				<view v-show ="currentIndex === 0">
					<uni-card v-for="(item, index) in leaveNoteList" 
						:key="item.id" note="true">
						<view class="card-header">
							<uni-tag type="default" text="未审核" ></uni-tag>
							<!-- <uni-tag :text="item.type == 0 ? '未审核': item.type == 1 ? '已通过': '已拒绝' " :type="item.type == 0? 'default': item.type == 1 ? 'success': 'error'"></uni-tag> -->
							<text class="right">{{item.startTime}}</text>
						</view>
						<view><text>姓名: {{item.username}}</text></view>
						<view><text>班级: {{item.majorAndClass}}</text></view>
						<view><text>学号: {{item.studentNumber}}</text></view>
						<view><text>是否离校: {{item.depart === "YES" ? "是" : "否"}}</text></view>
						<view><text>离校时间: {{item.startTime}}</text></view>
						<view><text>返校时间: {{item.endTime}}</text></view>
						<view><text>请假天数: {{item.days}}</text></view>
						<view slot="actions" class="card-actions">
							<view class="card-actions-item" @click="checkDetails(item.id)">
								<text class="card-actions-item-text">查看详情</text>
								<uni-icons type="arrow-right" size="20" color="#1b478e"></uni-icons>
							</view>
						</view>
					</uni-card>
					<view>
						<uni-load-more :status="loadMore1.status" :contentText="loadMore1.contentText" @clickLoadMore="clickLoadMore1()"></uni-load-more>
					</view>
				</view>
				<view  v-show ="currentIndex === 1">
					<uni-card v-for="(item, index) in approvedList"
						:key="item.id" note="true">
						<view class="card-header">
							<uni-tag :text="item.status === 'SUCCESS' ? '已销假' : item.examine === 'SUCCESS' ?  '已通过': item.examine === 'FAILURE' ? '已拒绝' : '审核中' " :type="item.status === 'SUCCESS' ? 'primary' : item.examine === 'SUCCESS' ? 'success': item.examine === 'FAILURE' ? 'error':'warning'"></uni-tag>
							<text class="right">{{item.startTime}}</text>
						</view>
						<view><text>姓名: {{item.username}}</text></view>
						<view><text>班级: {{item.majorAndClass}}</text></view>
						<view><text>学号: {{item.studentNumber}}</text></view>
						<view><text>是否离校: {{item.depart === "YES" ? "是" : "否"}}</text></view>
						<view><text>离校时间: {{item.startTime}}</text></view>
						<view><text>返校时间: {{item.endTime}}</text></view>
						<view><text>请假天数: {{item.days}}</text></view>
						<view slot="actions" class="card-actions">
							<view class="card-actions-item" @click="checkDetails(item.id)">
								<text class="card-actions-item-text">查看详情</text>
								<uni-icons type="arrow-right" size="20" color="#1b478e"></uni-icons>
							</view>
						</view>
					</uni-card>
					<view>
						<uni-load-more :status="loadMore2.status" :contentText="loadMore2.contentText" @clickLoadMore="clickLoadMore2()"></uni-load-more>
					</view>
				</view>
			</view>
			
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				currentIndex: 0,
				current:['未处理','已审核'],
				showPage:{
					notaudit:{
						pageData:{
							"pageNo": 1,
							"pageSize": 10,
							"completeEnum": "NO",
						},
						//总页数
						endPade:""
					},
					approved:{
						pageData:{
							"pageNo": 1,
							"pageSize": 10,
							"completeEnum": "YES",
						},
						//总页数
						endPade:""
						},
				},
				loadMore1:{
					status:'more',
					contentText:{
						contentdown: "点击加载更多",
						contentrefresh: "正在加载...",
						contentnomore: "没有更多数据了"}
				},
				loadMore2:{
					status:'more',
					contentText:{
						contentdown: "点击加载更多",
						contentrefresh: "正在加载...",
						contentnomore: "没有更多数据了"}
				},
				//未审核假条
				leaveNoteList: [],
				//已审核
				approvedList:[]
			}
		},
		onLoad() {
			//获取未处理数据
			uni.$http.post('/leave/selectNoteByRole', this.showPage.notaudit.pageData).then((res)=>{
				console.log(res.data.data)
				this.leaveNoteList = res.data.data.list;
				this.showPage.notaudit.endPage = res.data.data.endPage;
				if(this.showPage.notaudit.endPage === 0 || this.showPage.notaudit.endPage === 1){
					this.loadMore1.status = 'noMore';
				}
			}).catch((err)=>{
				console.log(err)
			})
			//获取已审核数据
			uni.$http.post('/leave/selectNoteByRole', this.showPage.approved.pageData).then((res)=>{
				console.log(res.data.data)
				this.approvedList = res.data.data.list;
				this.showPage.approved.endPage = res.data.data.endPage;
				if(this.showPage.approved.endPage === 0 || this.showPage.approved.endPage === 1){
					this.loadMore2.status = 'noMore';
				}
			}).catch((err)=>{
				console.log(err)
			})
		},
		methods: {
			checkDetails(id){
				//console.log(id)
				uni.navigateTo({
					url: "../allLeaveDetails/allLeaveDetails?id=" + id + '&type=' + this.currentIndex,
					animationType: 'pop-in',
					animationDuration: 200
				})
			},
			onClickItem(e){
				this.currentIndex = e.currentIndex
			},
			clickLoadMore1(){
				this.loadMore1.status = 'loading';
				this.getMoreData()
			},
			clickLoadMore2(){
				this.loadMore2.status = 'loading';
				this.getMoreData()
			},
			getMoreData(){
				if(this.currentIndex == 0){
					if(this.showPage.notaudit.pageData.pageNo < this.showPage.notaudit.endPage){
						this.showPage.notaudit.pageData.pageNo++;
						uni.$http.post('/leave/selectNoteByRole', this.showPage.notaudit.pageData).then((res)=>{
							console.log(res.data)
							if(res.data.code === 200){
								for(let i in res.data.data.list){
									this.leaveNoteList.push(res.data.data.list[i]);
								}
								//console.log(this.leaveNoteList);
								if(this.showPage.notaudit.pageData.pageNo === this.showPage.notaudit.endPage){
									this.loadMore1.status = 'noMore';
								}else{
									this.loadMore1.status = 'more';
								}
							}
						}).catch((err)=>{
							console.log(err)
						})
					}else{
						this.loadMore1.status = 'noMore';
					}
				}else if(this.currentIndex == 1){
					if(this.showPage.approved.pageData.pageNo < this.showPage.approved.endPage){
						this.showPage.approved.pageData.pageNo++;
						uni.$http.post('/leave/selectNoteByRole', this.showPage.approved.pageData).then((res)=>{
							console.log(res.data)
							if(res.data.code === 200){
								for(let i in res.data.data.list){
									this.approvedList.push(res.data.data.list[i]);
								}
								//console.log(this.approvedList);
								if(this.showPage.approved.pageData.pageNo === this.showPage.approved.endPage){
									this.loadMore2.status = 'noMore';
								}else{
									this.loadMore2.status = 'more';
								}
							}
						}).catch((err)=>{
							console.log(err)
						})
						}
						else{
							this.loadMore2.status = 'noMore';
						}
					}
						
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
