<script setup>
import { reactive } from 'vue'
import { specialApi, COLORS } from '../api'

const input = reactive({ del: '', less: '', hair: 'GREEN', eye: 'GREEN' })
const results = reactive({ del: null, sum: null, less: null, hair: null, eye: null })
const errors = reactive({ del: '', sum: '', less: '', hair: '', eye: '' })

async function run(key, action) {
  errors[key] = ''
  results[key] = null
  try {
    results[key] = await action()
  } catch (e) {
    errors[key] = e.message
  }
}

function heightIsValid(key) {
  if (Number(input[key]) > 0) return true
  errors[key] = 'Введите число больше 0'
  results[key] = null
  return false
}

const deleteOne = () => heightIsValid('del') && run('del', () => specialApi.deleteByHeight(input.del))
const calcSum = () => run('sum', () => specialApi.heightSum())
const findLess = () => heightIsValid('less') && run('less', () => specialApi.heightLess(input.less))
const calcHair = () => run('hair', () => specialApi.hairPercent(input.hair))
const calcEye = () => run('eye', () => specialApi.eyeCount(input.eye))
</script>

<template>
  <h1>Специальные операции</h1>

  <section class="card">
    <h2>Удалить один объект с заданным ростом</h2>
    <form class="inline" @submit.prevent="deleteOne">
      <input v-model="input.del" type="number" min="0.01" step="any" placeholder="height" />
      <button class="danger" type="submit">Удалить</button>
    </form>
    <p v-if="errors.del" class="banner error">{{ errors.del }}</p>
    <p v-if="results.del" class="result">Удалён объект {{ results.del.deletedId }}</p>
  </section>

  <section class="card">
    <h2>Сумма роста всех объектов</h2>
    <button @click="calcSum">Рассчитать</button>
    <p v-if="errors.sum" class="banner error">{{ errors.sum }}</p>
    <p v-if="results.sum" class="result">Сумма: {{ results.sum.sum }}</p>
  </section>

  <section class="card">
    <h2>Объекты с ростом меньше заданного</h2>
    <form class="inline" @submit.prevent="findLess">
      <input v-model="input.less" type="number" min="0.01" step="any" placeholder="height" />
      <button type="submit">Найти</button>
    </form>
    <p v-if="errors.less" class="banner error">{{ errors.less }}</p>
    <template v-if="results.less">
      <p class="result">Найдено: {{ results.less.length }}</p>
      <table v-if="results.less.length">
        <thead>
          <tr><th>ID</th><th>Имя</th><th>Волосы</th><th>Глаза</th><th>Рост</th></tr>
        </thead>
        <tbody>
          <tr v-for="p in results.less" :key="p.id">
            <td>{{ p.id }}</td>
            <td>{{ p.name }}</td>
            <td>{{ p.hairColor }}</td>
            <td>{{ p.eyeColor ?? '—' }}</td>
            <td>{{ p.height }}</td>
          </tr>
        </tbody>
      </table>
    </template>
  </section>

  <section class="card">
    <h2>Доля людей с заданным цветом волос</h2>
    <form class="inline" @submit.prevent="calcHair">
      <select v-model="input.hair">
        <option v-for="c in COLORS" :key="c" :value="c">{{ c }}</option>
      </select>
      <button type="submit">Рассчитать</button>
    </form>
    <p v-if="errors.hair" class="banner error">{{ errors.hair }}</p>
    <p v-if="results.hair" class="result">{{ results.hair.percent.toFixed(2) }} %</p>
  </section>

  <section class="card">
    <h2>Количество людей с заданным цветом глаз</h2>
    <form class="inline" @submit.prevent="calcEye">
      <select v-model="input.eye">
        <option v-for="c in COLORS" :key="c" :value="c">{{ c }}</option>
      </select>
      <button type="submit">Посчитать</button>
    </form>
    <p v-if="errors.eye" class="banner error">{{ errors.eye }}</p>
    <p v-if="results.eye" class="result">{{ results.eye.count }}</p>
  </section>
</template>
