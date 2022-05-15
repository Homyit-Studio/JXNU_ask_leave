<template>
	<view class="pointer">
		<template v-for="(item,index) in data" :key="item.value">
			<template v-if="!item.children || !item.children.length">
				<uni-menu-item :index="item">
					<text :class="{title: item.icon}">{{item.text}}</text>
					<view class="item-icon">{{item.total}}</view>
				</uni-menu-item>
			</template>
			<uni-sub-menu v-else :index="item">
				<template v-slot:title>
					<text :class="{title: item.icon}">{{item.text}}</text>
					<view class="item-icon">{{item.total}}</view>
				</template>
				<uni-menu-sidebar class="item-bg" :data="item.children" :key="item._id" />
			</uni-sub-menu>
		</template>
	</view>
</template>

<script>
	import uniMenuSidebar from '../uni-menu-sidebar/uni-menu-sidebar.vue'
	export default {
		name: 'uniMenuSidebar',
		props: {
			data: {
				type: Array,
				default () {
					return []
				}
			}
		},
		data() {
			return {};
		},
		computed: {

		},
		methods: {

		}
	}
</script>

<style lang="scss">
	@import url("uni-icons.css");

	.uni-menu-item {
		position: relative;
	}

	.item-icon {
		min-width: 15px;
		height: 15px;
		text-align: center;
		font-size: 12px;
		box-sizing: border-box;
		position: absolute;
		right: 10px;
		top: 0;
		background-color: #1b478e;
		color: #fff;
		border-radius: 50%;
	}
</style>
