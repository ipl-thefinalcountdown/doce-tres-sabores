<template>
  <b-form-group :label="label" :description="description">
	<multi-select :options="options" :selected-options="value" @select="handleInput" :placeholder="placeholder" />
  </b-form-group>
</template>

<script lang="ts">
import Vue from "vue";

import { MultiSelect } from 'vue-search-select';

export default Vue.extend({
	 props: {
	  value: Array,
      label: String,
      description: String,
	  placeholder: String,
	  options: Array
  },
  model: {
      prop: 'value',
      event: 'onFieldChanged'
  },
  methods: {
    handleInput(items : Array<any>, lastSelectItem: any)
    {
      this.$emit('onFieldChanged', items);
    }
  },
  components: {
	  MultiSelect
  }
});
</script>
