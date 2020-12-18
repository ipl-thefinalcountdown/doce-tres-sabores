<template>
  <div class="container-fluid">
    <div class="pt-3 overflow-auto">
      <!-- Table -->
      <b-table
        :items="items"
        :fields="fields"
        :no-select-on-click="true"
        :busy="awaitingSearch"
        :head-variant="headVariant"
        hover
        show-empty
        responsive="sm"
      >
        <!-- Busy loading template -->
        <template #table-busy>
          <div class="text-center text-secondary my-2">
            <b-spinner class="align-middle"></b-spinner>
            <strong>Loading...</strong>
          </div>
        </template>

        <!-- Cells of action column template -->
        <template #cell(valid)="scope">
          <div class="text-nowrap text-right">
            <i :class="`fas fa-lg ${(scope.item.valid) ? 'fa-times-circle text-danger' : 'fa-check-circle text-success'}`"></i>
          </div>
        </template>

        <!-- User slot -->
        <slot></slot>
      </b-table>
    </div>
  </div>
</template>

<script lang="ts">
import Vue from "vue";

export default Vue.extend({
  props: {
    /// table items
    items: Array,
    /// table fields (table header and footer columns)
    fields: Array,
  },
  data() {
    let obj = this;
    return {
      // == table configurations == //
      headVariant: 'dark',

      /// table busy locker (to display loading template)
      awaitingSearch: <boolean>true,

      /// timeout handler id
      timeout: <number | undefined>undefined,
    };
  },
  computed: {
    /// compute total number of rows
    rows(): number {
      return this.items.length;
    },
  },
  watch: {
    // every time items are updated, busy state is unlocked
    items: function (val) {
      this.awaitingSearch = false;
    },
  },
});
</script>
