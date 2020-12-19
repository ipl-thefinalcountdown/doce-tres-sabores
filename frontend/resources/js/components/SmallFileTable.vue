<template>
  <div class="container-fluid">
    <div class="pt-3 overflow-auto">
      <!-- Table -->
      <b-table
        :items="items"
        :fields="tableFields()"
        :no-select-on-click="true"
        :busy="awaitingSearch"
        hover
        show-empty
        small
        responsive="sm"
      >
        <!-- Busy loading template -->
        <template #table-busy>
          <div class="text-center text-secondary my-2">
            <b-spinner class="align-middle"></b-spinner>
            <strong>Loading...</strong>
          </div>
        </template>

        <!-- Head of action column template -->
        <template #head(__actions)="scope">
          <div class="text-nowrap text-right">
            {{ (rows && !noActionLabel) ? scope.label : '' }}
          </div>
        </template>

        <!-- Cells of action column template -->
        <template #cell(__actions)="scope">
          <div class="text-nowrap text-right">

            <!-- Edit icon button -->
            <b-link
              v-if="downloadClicked"
              @click.prevent="downloadClicked(scope.item, scope.index, $event)"
              class="text-secondary"
            >
              <i class="fas fa-download"></i>
            </b-link>

            <!-- Delete icon button -->
            <b-link
              v-if="deleteClicked"
              @click.prevent="deleteClicked(scope.item, scope.index, $event)"
              class="text-danger"
            >
              <i class="fas fa-trash"></i>
            </b-link>
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
    /// callback when row edit button is clicked
    downloadClicked: Function,
    /// callback when row delete button is clicked
    deleteClicked: Function,
    // boolean for defining header action label
    noActionLabel: Boolean
  },
  computed: {
    /// compute total number of rows
    rows(): number {
      return this.items.length;
    },
  },
  data() {
    let obj = this;
    return {
      /// table busy locker (to display loading template)
      awaitingSearch: <boolean>true,

      /// timeout handler id
      timeout: <number | undefined>undefined,

      /// custom table fields to include actions
      tableFields(): Array<any> | undefined {
        if (obj.fields) return obj.fields;
        else {
          let fields = Object.keys(<object>obj.items[0] ?? {})
            .filter((val) => val != "id");
          if (obj.deleteClicked == undefined && obj.downloadClicked == undefined)
            return fields;
          else
            return fields.concat(<any>{
                key: "__actions",
                name: "Actions",
              });
        }
      },
    };
  },
  watch: {
    // every time items are updated, busy state is unlocked
    items: function (val) {
      this.awaitingSearch = false;
    },
  },
});
</script>
