<script setup>
import { reactive, ref, onMounted } from 'vue'
import { coordinatesApi, locationApi } from '../api'

const coordinates = ref([])
const locations = ref([])
const error = ref('')

const coordForm = reactive({ x: '', y: '' })
const coordErrors = ref({})
const locForm = reactive({ x: '', y: '', z: '', name: '' })
const locErrors = ref({})

async function load() {
  try {
    coordinates.value = await coordinatesApi.list()
    locations.value = await locationApi.list()
  } catch (e) {
    error.value = e.message
  }
}

function showServerError(err, target) {
  error.value = err.message
  const map = {}
  ;(err.fieldErrors ?? []).forEach((f) => (map[f.field] = f.message))
  target.value = map
}

async function addCoordinates() {
  error.value = ''
  const e = {}
  if (coordForm.x === '') e.x = 'Введите x'
  if (coordForm.y === '') e.y = 'Введите y'
  coordErrors.value = e
  if (Object.keys(e).length) return
  try {
    await coordinatesApi.create({ x: Number(coordForm.x), y: Number(coordForm.y) })
    coordForm.x = ''
    coordForm.y = ''
    await load()
  } catch (err) {
    showServerError(err, coordErrors)
  }
}

async function addLocation() {
  error.value = ''
  const e = {}
  if (locForm.x === '') e.x = 'Введите x'
  if (locForm.y === '') e.y = 'Введите y'
  if (locForm.z === '' || !Number.isInteger(Number(locForm.z))) e.z = 'z должно быть целым числом'
  if (locForm.name !== '' && !locForm.name.trim()) e.name = 'Название должно быть не пустым'
  locErrors.value = e
  if (Object.keys(e).length) return
  try {
    await locationApi.create({
      x: Number(locForm.x),
      y: Number(locForm.y),
      z: Number(locForm.z),
      name: locForm.name.trim() || null,
    })
    Object.assign(locForm, { x: '', y: '', z: '', name: '' })
    await load()
  } catch (err) {
    showServerError(err, locErrors)
  }
}

async function removeCoordinates(id) {
  error.value = ''
  try {
    await coordinatesApi.remove(id)
    await load()
  } catch (e) {
    error.value = e.message
  }
}

async function removeLocation(id) {
  error.value = ''
  try {
    await locationApi.remove(id)
    await load()
  } catch (e) {
    error.value = e.message
  }
}

onMounted(load)
</script>

<template>
  <h1>Координаты и локации</h1>
  <p v-if="error" class="banner error">{{ error }}</p>

  <section class="card">
    <h2>Координаты</h2>
    <form class="inline" novalidate @submit.prevent="addCoordinates">
      <label>x <input v-model="coordForm.x" type="number" step="any" /></label>
      <label>y <input v-model="coordForm.y" type="number" step="any" /></label>
      <button type="submit">Добавить</button>
    </form>
    <small v-if="coordErrors.x" class="error">{{ coordErrors.x }}</small>
    <small v-if="coordErrors.y" class="error">{{ coordErrors.y }}</small>

    <table>
      <thead>
        <tr><th>ID</th><th>x</th><th>y</th><th></th></tr>
      </thead>
      <tbody>
        <tr v-for="c in coordinates" :key="c.id">
          <td>{{ c.id }}</td>
          <td>{{ c.x }}</td>
          <td>{{ c.y }}</td>
          <td><button class="danger" @click="removeCoordinates(c.id)">Удалить</button></td>
        </tr>
        <tr v-if="!coordinates.length"><td colspan="4" class="empty">Нет координат</td></tr>
      </tbody>
    </table>
  </section>

  <section class="card">
    <h2>Локации</h2>
    <form class="inline" novalidate @submit.prevent="addLocation">
      <label>x <input v-model="locForm.x" type="number" step="any" /></label>
      <label>y <input v-model="locForm.y" type="number" step="any" /></label>
      <label>z <input v-model="locForm.z" type="number" step="1" /></label>
      <label>Название <input v-model="locForm.name" type="text" maxlength="255" /></label>
      <button type="submit">Добавить</button>
    </form>
    <small v-for="(message, field) in locErrors" :key="field" class="error">{{ message }}</small>

    <table>
      <thead>
        <tr><th>ID</th><th>Название</th><th>x</th><th>y</th><th>z</th><th></th></tr>
      </thead>
      <tbody>
        <tr v-for="l in locations" :key="l.id">
          <td>{{ l.id }}</td>
          <td>{{ l.name ?? '—' }}</td>
          <td>{{ l.x }}</td>
          <td>{{ l.y }}</td>
          <td>{{ l.z }}</td>
          <td><button class="danger" @click="removeLocation(l.id)">Удалить</button></td>
        </tr>
        <tr v-if="!locations.length"><td colspan="6" class="empty">Нет локаций</td></tr>
      </tbody>
    </table>
  </section>
</template>
