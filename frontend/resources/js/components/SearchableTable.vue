<template>
	<b-table striped hover :items="items" :fields="fields"></b-table>
</template>

<script lang="ts">
import Vue from "vue"
import Component from "vue-class-component"

@Component
export default class VariantsView extends Vue {
	props: ['fields', 'items']

	// data() {
    //   return {
    //     fields,
    //     items
    //   }
    // }
}
</script>
