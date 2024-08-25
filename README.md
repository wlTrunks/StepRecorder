# StepRecorder

## Стек приложения
1. Разделение на модули по принципу Clean Arch: data, domain, core-presentation, app
2. Хранение данных Room, Datastore 
3. Coroutines, Flow 
4. DI Koin
5. MVI
6. Compose
7. Профиль и данные о шагах GoogleSignIn и Fitness

## Описание приложения
Сингл активити.
Архитектура и навигация придуманна на ходу, много деталей не учтено. 
MVI для экранов
Приложения состоит из 3х экранов и сервиса.
1. [com.lingdtkhe.sgooglefitsaver.ui.screen.start.StartScreen] стартовый экран логин через GoogleSignIn, отображение
профиля с текущим счетчиком шагов за день
2. [com.lingdtkhe.sgooglefitsaver.ui.screen.trackstep.TrackStepScreen] запуск сервиса по отслеживанию шагов,
отображение списка шагов
3. [com.lingdtkhe.sgooglefitsaver.ui.screen.trackhistory.TrackHistoryScreen] запрос истории шагов с виджетом календаря

## MVI 
1. [com.lingdtkhe.core.ui.screen.ScreenComponent] - компонет экрана, объект предоставляется в компонет навигации.
2. [com.lingdtkhe.core.ui.screen.ScreenModel] - модель для экрана 
3. [com.lingdtkhe.core.ui.screen.ScreenAction] - действия на экране
4. [com.lingdtkhe.core.ui.screen.ScreenState] - состояние экрана
5. [com.lingdtkhe.core.ui.screen.ScreenEvent] - разовые события 
6. [com.lingdtkhe.core.ui.screen.ScreenLifeCycle] - жизненый цикл для экрана
   
## Core Presentation
1. Модуль core-presentation, [com.lingdtkhe.core.ui.activity.ActivityProvider] и 
[com.lingdtkhe.core.ui.activity.InitComponentActivity] интерфесы для иниацилзации и предостовления для активити.
2. [com.lingdtkhe.core.ui.navigation.NavigationComponent] компонент навигации, учитывает отработку кнопки назад, и
жизненые циклы активити, ну или пытается.
3. В папке screen компоненты для MVI.

## Domain
1. [com.lingdtkhe.domain.interactors.AuthInteractor] для получение профиля и логин
2. [com.lingdtkhe.domain.interactors.TrackStepServiceLauncherInteractor] проверяет пермишены и тригерит запуск 
    и остановку сервиса, получение состояние сервиса.
3. [com.lingdtkhe.domain.interactors.TrackStepSensorServiceInteractor] устанавливает статус сервиса и трерит запуск и 
    остановку подписку на сенсоры.
4. папка components [com.lingdtkhe.domain.components.IntentLauncher] запрос пермишенов и suspend функция.
5. repository 
6. service компоненты для инициализации и предоставления состояние сервиса.
7. usecases

## Data
1. Папка db база данных Room
2. repositories, [com.lingdtkhe.data.repositories.AuthRepoImpl] авторизация через GoogleSignIn и получение 
профиля и кол-во шагов на текущий день из [com.google.android.gms.fitness.Fitness]
3. [com.lingdtkhe.data.repositories.StepsRepoImpl] запрос шагов за период времени и отслеживание при старте.
4. [com.lingdtkhe.data.repositories.TrackStepSensorRepoImpl] регистрация в Fitness api и подписка на сенсоры телефона.
5. [com.lingdtkhe.data.repositorie.UserPreferencesRepoImpl] сохранение старта времени запуска сервиса.
6. [com.lingdtkhe.data.sources.LocalDbStepsSource] запись и получние из БД

## App
1. [com.lingdtkhe.sgooglefitsaver.App] инициализируется Koin модули, Timber, канал для нотифицации.
2. [com.lingdtkhe.sgooglefitsaver.ui.MainActivity] ; [com.lingdtkhe.sgooglefitsaver.ui.MainViewModel];
[com.lingdtkhe.sgooglefitsaver.ui.InitComponentActivityImpl] активити, vm и реализация компонтета активити.
3. папка service, [com.lingdtkhe.sgooglefitsaver.service.TrackStepSensorService] и компоненты реализация компонентов.

## Tests
1. [com.lingdtkhe.sgooglefitsaver.TrackStepScreenModelTest] 
2. [com.lingdtkhe.domain.TrackStepServiceLauncherInteractorTest]

gradle plugins





   

