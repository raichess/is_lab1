<template>
    <h1>Удалить пипл</h1>

    <form class="inline" @submit.prevent="findPerson">
      <input
          v-model="idInput"
          type="number"
          min="1"
          step="1"
          placeholder="ID человека"
          required
      />
      <button type="submit">Найти</button>
    </form>

    <p v-if="error" class="banner error">
      {{ error }}
    </p>

    <p v-if="success" class="banner">
      {{ success }}
    </p>

    <section v-if="person" class="card">
      <h2>Удалить{{ person.id }}</h2>

      <p>Имя: {{ person.name }}</p>
      <p>Рост: {{ person.height }}</p>

      <button class="danger" @click="removePerson">
        Удалить
      </button>
    </section>
</template>