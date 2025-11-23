# Bienvenue dans votre test technique Android !

Vous disposez de 7 jours pour effectuer ce test technique et nous renvoyer le lien vers votre repository

## Voici que l'on attend de vous dans ce test :

- Cloner le repository et pousser vos modifications sur un repository de votre compte github
- Corriger les erreurs de compilation
- Utiliser la clean archi
- Modifier le datasource pour qu'il retourne un Flow<>
- S'assurer que le code fonctionne dans les meilleurs conditions
- Ne pas hésiter à réparer/refactorer/réordonner le projet
- Ajouter Ktlint et Mockito
- Au clic d'un équipement, afficher une fiche d'équipement
- Ajouter un filtre en fonction du UserRole (choisir le rôle dans la UI de la liste des équipements) :
    - Si UserRole est ADMIN, alors afficher tous les équipements
    - Si UserRole est MAINTAINER, alors afficher les équipements de type 0 et 1
    - Si UserRole est AUDITOR, alors afficher les équipements de type 0

Vous pouvez vous aider des librairies de votre choix, en justifiant !


## Voici un exemple des écrans que l'on attend de vous :

Liste des équipements : 

<img width="743" height="1175" alt="image" src="https://github.com/user-attachments/assets/0c13af0e-5a03-4d18-9a23-6f055e77370f" />

Fiche équipement : 

<img width="743" height="1171" alt="image" src="https://github.com/user-attachments/assets/86ab3ad1-bd3a-4486-9262-4ce459c8fcf1" />

## Recommandations :

Pour ce test technique, nous vous invitions à respecter les bonnes pratiques recommandées par la documentation Android.

Si vous avez des questions concernant ce test technique, vous pouvez nous les poser à cette adresse : arthur.mercier@beeldi.com



## Bugs corrigés

- AGP incompatible : la version 8.13.1 déclarée dans `gradle/libs.versions.toml` empêchait la synchronisation. Remplacée par 8.6.0 comme demandé.
- DSL `compileSdk` invalide : usage de `compileSdk { version = release(36) }` non supporté. Corrigé en utilisant la syntaxe standard `compileSdk = 34`.
- Incohérence de modèle : `EquipmentEntity` nécessitait `type: Int` mais `EquipmentDataSource` ne passait pas l’argument. Ajout de la lecture du champ `type`.
- Types JSON incompatibles : `type` stocké comme chaîne dans `equipments.json` provoquait un crash. Passé en entier.
- Entrée JSON incomplète : l’équipement `id = 5` n’avait pas de `name`, ce qui faisait échouer le parsing. Champ ajouté.
- `EquipmentDataSource` lisait les fichiers sur `Dispatchers.Main` sans dépendance `kotlinx-coroutines-android`. Passage sur `Dispatchers.IO` et ajout de la dépendance.
- ViewModel non initialisable : `EquipmentListViewModel` n’acceptait pas le `EquipmentDataSource` alors que `MainActivity` essayait de lui passer. Constructeur mis à jour + factory `viewModels`.
- État non exposé : absence de propriété publique pour l’UI (`state`). Ajout de `val state: StateFlow<EquipmentListState>` et implémentation de `loadEquipments()` avec `viewModelScope`.
- Propriété UI erronée : `EquipmentListScreen` utilisait `state.equipments` au lieu de `state.equipmentEntities`. Aligné et import `items` ajouté.
- Fonction `items` non importée : `androidx.compose.foundation.lazy.items` manquait, causant des références rouges. Import ajouté.
- `enableEdgeToEdge()` non résolu : ajout de la dépendance `androidx.activity:activity-ktx` pour rétablir la fonction.



## Refonte Clean Architecture MVVM

### Objectif

Refactorisation complète de l'application pour adopter une architecture clean avec le pattern MVVM, respectant les bonnes pratiques Android et améliorant la maintenabilité, testabilité et scalabilité du code.

### Architecture mise en place

#### Structure en 3 couches

- **Domain Layer** : Logique métier pure (indépendante d'Android)
  - Modèles de domaine (`Equipment`, `UserRole`)
  - Interfaces Repository
  - Use Cases (cas d'usage métier)

- **Data Layer** : Gestion des données
  - Implémentations des repositories
  - Sources de données (local/remote)
  - Modèles de données (DTO)
  - Mappers (conversion Data ↔ Domain)

- **Presentation Layer** : Interface utilisateur
  - UI (Jetpack Compose)
  - ViewModels
  - États UI (sealed classes)
  - Composables réutilisables

### Changements principaux

#### Architecture
- Séparation en 3 couches (Domain, Data, Presentation)
- Un fichier = une classe/composant/fonction
- Flux de données unidirectionnel
- Séparation claire des responsabilités

#### Domain Layer
- Création de `Equipment` (modèle de domaine)
- Création de `EquipmentRepository` (interface)
- Création de `GetEquipmentsUseCase` (use case)
- Migration de `UserRole` vers le domain layer

#### Data Layer
- Création de `EquipmentEntity` (modèle de données)
- Migration `EquipmentDataSource` → `EquipmentLocalDataSource`
- Création de `EquipmentRepositoryImpl` (implémentation)
- Création de `EquipmentMapper` (conversion Data ↔ Domain)
- Utilisation de `buildList` pour l'immutabilité

#### Presentation Layer
- Migration de `MainActivity` vers `presentation/main/`
- Création de `EquipmentListUiState` (sealed class)
- Refactorisation du ViewModel pour utiliser les Use Cases
- Décomposition de l'écran en composables réutilisables :
  - `EquipmentListLoading`
  - `EquipmentListError`
  - `EquipmentListContent`
- Le composable reçoit l'UiState au lieu du ViewModel

#### Injection de dépendances
- Création de `AppModule` (construction des dépendances)
- Création de `EquipmentViewModelFactory` (injection dans ViewModel)
- Simplification de `MainActivity`

#### Gestion des états
- Remplacement par `sealed class` pour `EquipmentListUiState`
- États : `Loading`, `Success`, `Error`
- Utilisation de `StateFlow` au lieu de `LiveData`
- Utilisation de `asStateFlow()` pour l'immutabilité
- Utilisation de `update()` pour les mutations (thread-safe)

### Bonnes pratiques appliquées

- StateFlow au lieu de LiveData
- Flux de données unidirectionnel
- Immutabilité des états
- Pas de logique métier dans les composables
- Composables décomposés et réutilisables
- Utilisation de `viewModelScope` pour les coroutines
- Suspend/Flow au lieu de callbacks
- Material Design 3 (couleurs du thème)




## Résumé des changements - Session du jour

### Fonctionnalités ajoutées

#### 1. Fiche d'équipement au clic
- Navigation vers un écran de détail au clic sur une carte d'équipement
- Création de `EquipmentDetailScreen` avec affichage détaillé des informations
- Utilisation de Navigation Compose avec passage d'arguments (equipmentId)
- Création de `EquipmentDetailViewModel` et `EquipmentDetailUiState`
- Factory `EquipmentDetailViewModelFactory` pour l'injection de dépendances

#### 2. Filtre par UserRole
- Ajout d'un sélecteur de rôle (`RoleSelector`) dans l'interface utilisateur
- Implémentation de la logique de filtrage dans `EquipmentListViewModel` :
  - ADMIN : affiche tous les équipements
  - MAINTAINER : affiche les équipements de type 0 et 1
  - AUDITOR : affiche les équipements de type 0
- Utilisation de `combine` pour combiner plusieurs `StateFlow` (équipements, rôle sélectionné, état de chargement, erreur)
- Mise à jour de `EquipmentListUiState.Success` pour inclure `selectedRole`

---

### Corrections architecturales

#### 1. DataSource retourne un Flow (consigne)
- Modification de `EquipmentLocalDataSource.getEquipments()` pour retourner `Flow<List<EquipmentEntity>>` au lieu de `suspend fun`
- Utilisation de `flow { }` avec `emit()` pour la réactivité
- Mise à jour de `EquipmentRepository` et `EquipmentRepositoryImpl` pour gérer `Flow<Result<List<Equipment>>>`
- Mise à jour de `GetEquipmentsUseCase` pour retourner un `Flow`
- Adaptation de `EquipmentListViewModel` pour utiliser `.collect { }` sur le Flow

#### 2. Corrections de code
- Correction des imports dans `EquipmentListViewModel` (utilisation de `Equipment` au lieu du chemin complet)
- Suppression des imports wildcard dans `EquipmentDetailScreen.kt` (remplacement par des imports explicites)
- Renommage de `GetEquipmentUseCase.kt` → `GetEquipmentsUseCase.kt` (cohérence avec le nom de la classe)
- Suppression du fichier vide `EquipmentRemoteDataSource.kt`
- Ajout de l'import manquant `kotlinx.coroutines.flow.collect` dans `EquipmentListViewModel`

#### 3. Gestion des WindowInsets
- Ajout de `Scaffold` dans `EquipmentListScreen` pour gérer automatiquement les WindowInsets système
- Application de `paddingValues` à tous les états (Loading, Error, Success)
- Correction du problème de clics non détectés en haut de l'écran

---

### Mise à jour de l'UI avec le thème Material Design 3

#### 1. Configuration du thème
- Ajout de `primaryContainer = cardBackground` dans `LightColorScheme` pour définir la couleur orange des cartes
- Ajout de `onPrimaryContainer` pour la couleur du texte sur fond orange
- Désactivation de `dynamicColor` dans `MainActivity` pour utiliser le thème personnalisé
- Ajout de l'import `androidx.compose.ui.graphics.Color` dans `Theme.kt`

#### 2. Application des couleurs aux cartes
- Modification de `EquipmentCard` pour utiliser `MaterialTheme.colorScheme.primaryContainer` comme couleur de fond
- Modification de `EquipmentDetailScreen` pour appliquer la même couleur orange à la carte de détail
- Utilisation de `CardDefaults.cardColors(containerColor = ...)` pour respecter Material Design 3
- Application de `onPrimaryContainer` pour le texte dans les cartes (meilleur contraste)

---

### Améliorations techniques

- Utilisation de `combine` et `stateIn` pour gérer plusieurs StateFlow dans le ViewModel
- Utilisation de `SharingStarted.WhileSubscribed(5000)` pour optimiser les performances
- Gestion des erreurs avec `Result<T>` et `catch` dans les Flows
- Navigation typée avec `Screen` sealed class
- Utilisation de `remember(key)` et `LaunchedEffect` pour la récupération d'équipement dans la navigation

---
