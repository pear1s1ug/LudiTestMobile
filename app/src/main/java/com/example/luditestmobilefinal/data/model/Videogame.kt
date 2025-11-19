package com.example.luditestmobilefinal.data.model

data class Videogame(
    val id: Int,
    val name: String,
    val genres: List<GameGenre>,
    val platform: List<GamePlatform>,
    val description: String,
    val rating: Double,
    val trailerUrl: String? = null,
    val imageUrl: String,
    val featured: Boolean = false,
    val releaseDate: String
)

enum class GameGenre {
    // ACCIÓN
    ACTION,
    SHOOTER,
    FPS,
    TPS,
    TACTICAL_SHOOTER,
    BATTLE_ROYALE,
    FIGHTING,
    HACK_AND_SLASH,
    BEAT_EM_UP,
    RUN_AND_GUN,
    STEALTH,
    SURVIVAL_ACTION,
    BULLET_HELL,

    // AVENTURA
    ADVENTURE,
    PLATFORMER,
    METROIDVANIA,
    OPEN_WORLD,
    SANDBOX,
    SURVIVAL,
    HORROR,
    SURVIVAL_HORROR,
    PSYCHOLOGICAL_HORROR,
    MYSTERY,
    INTERACTIVE_MOVIE,

    // RPG
    RPG,
    ACTION_RPG,
    TACTICAL_RPG,
    MMORPG,
    JRPG,
    ROGUELIKE,
    ROGUELITE,
    SOULS_LIKE,
    DUNGEON_CRAWLER,

    // ESTRATEGIA
    STRATEGY,
    REAL_TIME_STRATEGY,
    TURN_BASED_STRATEGY,
    GRAND_STRATEGY,
    FOUR_X,
    TOWER_DEFENSE,
    MOBA,
    CARD_GAME,
    BOARD_GAME,
    TABLETOP,
    TACTICAL,

    // SIMULACIÓN
    SIMULATION,
    LIFE_SIM,
    SOCIAL_SIM,
    DATING_SIM,
    MANAGEMENT_SIM,
    CITY_BUILDER,

    // DEPORTES
    SPORTS,
    RACING,
    EXTREME_SPORTS,
    BASEBALL,

    // MÚSICA Y RITMO
    MUSIC,
    RHYTHM,
    RHYTHM_ACTION,
    KARAOKE,

    // PUZZLE
    PUZZLE,
    LOGIC,
    LOGIC_PUZZLE,
    BRAIN_TEASER,
    TRIVIA,
    IDLE_GAME,
    INCREMENTAL,
    PHYSICS,

    // PARTY Y SOCIAL
    PARTY,
    MULTIPLAYER,
    CO_OP,
    CASUAL,
    SOCIAL,

    // CREATIVO
    CREATIVE_SANDBOX,
    ART,
    EDUCATIONAL,
    COMPILATION,

    // TEMÁTICAS
    HISTORICAL,
    SCI_FI,
    FANTASY,
    ANIME,
    MYTHOLOGY,
    DRAMA,
    COMEDY,
    PHILOSOPHICAL,
    PSYCHOLOGICAL,
    COMING_OF_AGE,
    WAR,
    MONSTER_HUNTING,
    SATIRE,

    // PLATAFORMA/TECNOLOGÍA
    VR,
    ARCADE,
    FIRST_PERSON,

    // GENERAL
    MECHA,
    MULTIGENRE,
    COMPETITIVE,
    NARRATIVE,
    NARRATIVE_ADVENTURE,
    VISUAL_NOVEL,
    POINT_AND_CLICK,
    WALKING_SIMULATOR,
    INDIE
}

enum class GamePlatform(val displayName: String) {
    PC("PC"),
    PS4("PlayStation 4"),
    PS5("PlayStation 5"),
    XBOX_ONE("Xbox One"),
    XBOX_SERIES_X("Xbox Series X"),
    XBOX_SERIES_S("Xbox Series S"),
    SWITCH("Nintendo Switch"),
    SWITCH_2("Nintendo Switch 2"),
    SMARTPHONES("Smartphones"),
    MULTI_PLATFORM("Multiplataforma"),
    MAC("Mac"),
    LINUX("Linux"),
    ANDROID("Android"),
    IOS("iOS"),
    UNKNOWN("Plataforma no especificada")
}