<script setup>
import { reactive, ref, onMounted } from 'vue'
import { personApi, coordinatesApi, locationApi, COLORS, COUNTRIES, ApiError } from '../api'

const props = defineProps({ person: { type: Object, default: null } })
const emit = defineEmits(['saved'])

const form = reactive({
  name: '',
  coordinatesId: '',
  eyeColor: '',
  hairColor: '',
  locationId: '',
  height: '',
  nationality: '',
})
const errors = ref({})
const generalError = ref('')
const coordinates = ref([])
const locations = ref([])
const saving = ref(false)

onMounted(async () => {
  const p = props.person
  if (p) {
    form.name = p.name
    form.coordinatesId = p.coordinates.id
    form.eyeColor = p.eyeColor ?? ''
    form.hairColor = p.hairColor
    form.locationId = p.location.id
    form.height = p.height
    form.nationality = p.nationality ?? ''
  }
  try {
    coordinates.value = await coordinatesApi.list()
    locations.value = await locationApi.list()
  } catch (e) {
    generalError.value = e.message
  }
})

function validate() {
  const e = {}
  if (!form.name.trim()) e.name = 'Имя не может быть пустым!'
  if (!form.coordinatesId) e.coordinatesId = 'Выберите координаты!'
  if (!form.hairColor) e.hairColor = 'Выберите цвет волос!'
  if (!form.locationId) e.locationId = 'Выберите локацию!'
  if (form.height === '' || !(Number(form.height) > 0)) e.height = 'Рост должен быть числом больше 0!'
  errors.value = e
  return Object.keys(e).length === 0
}

async function submit() {
  generalError.value = ''
  if (!validate()) return

  const body = {
    name: form.name.trim(),
    coordinatesId: Number(form.coordinatesId),
    eyeColor: form.eyeColor || null,
    hairColor: form.hairColor,
    locationId: Number(form.locationId),
    height: Number(form.height),
    nationality: form.nationality || null,
  }

  saving.value = true
  try {
    const saved = props.person ? await personApi.update(props.person.id, body) : await personApi.create(body)
    emit('saved', saved)
  } catch (e) {
    generalError.value = e.message
    const map = {}
    if (e instanceof ApiError) e.fieldErrors.forEach((fe) => (map[fe.field] = fe.message))
    errors.value = map
  } finally {
    saving.value = false
  }
}
</script>

<template>
  <form class="form" novalidate @submit.prevent="submit">
    <p v-if="generalError" class="banner error">{{ generalError }}</p>

    <label>
      Имя *
      <input v-model="form.name" type="text" maxlength="255" />
      <small v-if="errors.name" class="error">{{ errors.name }}</small>
    </label>

    <label>
      Координаты
      <select v-model="form.coordinatesId">
        <option value="">— выберите —</option>
        <option v-for="c in coordinates" :key="c.id" :value="c.id">{{ c.id }}: ({{ c.x }}; {{ c.y }})</option>
      </select>
      <small v-if="errors.coordinatesId" class="error">{{ errors.coordinatesId }}</small>
      <small v-if="!coordinates.length">
        Координат нет.. <RouterLink to="/references">Координаты и локации</RouterLink>.
      </small>
    </label>

    <label>
      Цвет глаз
      <select v-model="form.eyeColor">
        <option value="">— не указан —</option>
        <option v-for="c in COLORS" :key="c" :value="c">{{ c }}</option>
      </select>
      <small v-if="errors.eyeColor" class="error">{{ errors.eyeColor }}</small>
    </label>

    <label>
      Цвет волос
      <select v-model="form.hairColor">
        <option value="">— выберите —</option>
        <option v-for="c in COLORS" :key="c" :value="c">{{ c }}</option>
      </select>
      <small v-if="errors.hairColor" class="error">{{ errors.hairColor }}</small>
    </label>

    <label>
      Локация
      <select v-model="form.locationId">
        <option value="">— выберите —</option>
        <option v-for="l in locations" :key="l.id" :value="l.id">
          {{ l.id }}: {{ l.name ?? 'без названия' }} ({{ l.x }}; {{ l.y }}; {{ l.z }})
        </option>
      </select>
      <small v-if="errors.locationId" class="error">{{ errors.locationId }}</small>
      <small v-if="!locations.length">
        Пока нет локаций..<RouterLink to="/references">Координаты и локации</RouterLink>.
      </small>
    </label>

    <label>
      Рост
      <input v-model="form.height" type="number" min="0.01" step="any" />
      <small v-if="errors.height" class="error">{{ errors.height }}</small>
    </label>

    <label>
      Национальность
      <select v-model="form.nationality">
        <option value="">— не указана —</option>
        <option v-for="c in COUNTRIES" :key="c" :value="c">{{ c }}</option>
      </select>
      <small v-if="errors.nationality" class="error">{{ errors.nationality }}</small>
    </label>

    <button type="submit" :disabled="saving">{{ person ? 'Сохранить' : 'Создать' }}</button>
  </form>
</template>
