<template>
  <div>
    <h2 id="page-heading" data-cy="ProductHeading">
      <span id="product-heading">Products</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon> <span>Refresh List</span>
        </button>
        <router-link :to="{ name: 'ProductCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-product"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span> Create a new Product </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && products && products.length === 0">
      <span>No products found</span>
    </div>

    <div v-if="products && products.length > 0">
      <div class="list-group">
        <a
          class="list-group-item list-group-item-action flex-column align-items-start"
          v-for="product in products"
          :key="product.id"
          data-cy="entityTable"
        >
          <div class="row">
            <div class="col-2 col-xs-12 justify-content-center">
              <img src="https://d1nhio0ox7pgb.cloudfront.net/_img/o_collection_png/green_dark_grey/256x256/plain/mobile_phone.png" />
            </div>

            <div class="col col-xs-12">
              <div class="d-flex w-100 justify-content-between">
                <router-link :to="{ name: 'ProductView', params: { productId: product.id } }"
                  ><h5 class="mb-1">{{ product.name }}</h5></router-link
                >
                <small>{{ product.category }}</small>
                <small class="mb-1">{{ product.description }}</small>
                <p class="mb-1">Price: {{ product.price }}</p>
              </div>
            </div>
          </div>
        </a>
      </div>
    </div>

    <p></p>
    <br />

    <div class="table-responsive" v-if="products && products.length > 0">
      <table class="table table-striped" aria-describedby="products">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span>ID</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row">
              <span>Img</span>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span>Name</span> <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('price')">
              <span>Price</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'price'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('category')">
              <span>Category</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'category'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('color')">
              <span>Color</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'color'"></jhi-sort-indicator>
            </th>
            <th scope="row">
              <span v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated">Action</span>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in products" :key="product.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ProductView', params: { productId: product.id } }">{{ product.id }}</router-link>
            </td>
            <td><img src="https://d1nhio0ox7pgb.cloudfront.net/_img/o_collection_png/green_dark_grey/256x256/plain/mobile_phone.png" /></td>
            <td>{{ product.name }}</td>
            <td>{{ product.price }}</td>
            <td>{{ product.category }}</td>
            <td>{{ product.color }}</td>
            <td class="text-right">
              <div class="btn-group" v-if="hasAnyAuthority('ROLE_ADMIN') && authenticated">
                <router-link :to="{ name: 'ProductView', params: { productId: product.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ProductEdit', params: { productId: product.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(product)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="twentyoneApp.product.delete.question" data-cy="productDeleteDialogHeading">Confirm delete operation</span></span
      >
      <div class="modal-body">
        <p id="jhi-delete-product-heading">Are you sure you want to delete this Product?</p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-product"
          data-cy="entityConfirmDeleteButton"
          v-on:click="removeProduct()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="products && products.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./product.component.ts"></script>
