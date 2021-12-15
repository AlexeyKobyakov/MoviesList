# Movies List

https://github.com/AlexeyKobyakov/MoviesList

## Описание

Выполнение тестового задания для AppSelect

## Задание

Тестовое задание Android Developer Junior

Создать приложение для просмотра списка фильмов в виде ленты


Язык Kotlin

Для ленты используем RecyclerView, для вызова api используем retrofit, для картинок glide или что-то аналогичное


Приложение состоит из двух экранов

Экран 1:
 
Сплеш экран с названием приложения и иконкой приложения по центру

Экран 2:

RecyclerView со списком фильмов. 

Для каждого фильма должно быть название, описание, картинка. 

Список фильмов получаем по api от https://developer.nytimes.com/ -  Movie Reviews API

Регистрируемся на сайте, получаем ключ и выполняем запрос:

https://api.nytimes.com/svc/movies/v2/reviews/all.json

Список фильмов отображаем в RecyclerView. Реализовываем пагинацию!

Готовый проект заливаем на github и кидаем ссылку в ТГ для проверки

Для быстрой оценки проекта, пожалуйста, оставьте ключ api в build.gradle в дебаг ветке

## Реализация

![Screenshot 1](/1.png?raw=false)
![Screenshot 2](/2.png?raw=false)
