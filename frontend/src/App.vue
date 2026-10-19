<template>
  <header class="menu">
      <nav class="main-nav">
        <RouterLink to="/">Список</RouterLink>
        <RouterLink to="/create">Создать</RouterLink>
        <RouterLink to="/person">Найти </RouterLink>
        <RouterLink to="/edit">Изменить</RouterLink>
        <RouterLink to="/delete">Удалить</RouterLink>
        <RouterLink to="/references">Координаты и локации</RouterLink>
        <RouterLink to="/special">Специальные операции</RouterLink>
      </nav>
  </header>
  <main>
    <RouterView />
  </main>
</template>
