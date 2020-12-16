<template>
  <div>
    <b-navbar toggleable="lg" variant="light">
      <b-navbar-brand to="/">Chantilly</b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item :to="{name: 'list-products'}">Products</b-nav-item>
          <b-nav-item :to="{name: 'list-variants'}">Variants</b-nav-item>
          <b-nav-item :to="{name: 'list-projects'}">Projects</b-nav-item>
          <b-nav-item :to="{name: 'list-structures'}">Structures</b-nav-item>
          <b-nav-item :to="{name: 'list-clients'}">Clients</b-nav-item>
          <b-nav-item :to="{name: 'simulation'}">Simulation</b-nav-item>
        </b-navbar-nav>

        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">
          <b-nav-form @submit.prevent="$router.push({name: 'login'})">
            <b-button size="sm" class="my-2 my-sm-0" type="submit">Login</b-button>
          </b-nav-form>

          <b-nav-item-dropdown right>
            <!-- Using 'button-content' slot -->
            <template #button-content>
              <em>User</em>
            </template>
            <b-dropdown-item href="#">Profile</b-dropdown-item>
            <b-dropdown-item href="#">Sign Out</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </div>
</template>
