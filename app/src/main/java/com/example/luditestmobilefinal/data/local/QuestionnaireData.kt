package com.example.luditestmobilefinal.data.local

import com.example.luditestmobilefinal.data.model.AnswerOption
import com.example.luditestmobilefinal.data.model.Personality
import com.example.luditestmobilefinal.data.model.Question

object QuestionnaireData {
    private const val ANSWER_SCORE = 1

    val questions = listOf(
        Question(
            id = 1,
            text = "Cuando juegas un juego nuevo por primera vez, ¿cuál es tu enfoque?",
            options = listOf(
                AnswerOption(
                    text = "Entro directo a las misiones principales para avanzar rápido",
                    personalityWeights = mapOf(Personality.DOMINANT to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Interactúo con NPCs y exploro zonas con más vida social",
                    personalityWeights = mapOf(Personality.INFLUENTIAL to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Recorro el mapa con calma para familiarizarme con todo",
                    personalityWeights = mapOf(Personality.STEADY to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Reviso menús, controles y tutoriales para entender todo primero",
                    personalityWeights = mapOf(Personality.CONSCIENTIOUS to ANSWER_SCORE)
                )
            )
        ),

        Question(
            id = 2,
            text = "En un equipo de juego (raid, party, squad), ¿qué rol te sale natural?",
            options = listOf(
                AnswerOption(
                    text = "Dirigir al equipo y tomar decisiones rápidas",
                    personalityWeights = mapOf(Personality.DOMINANT to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Mantener el ánimo alto y coordinar la comunicación",
                    personalityWeights = mapOf(Personality.INFLUENTIAL to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Ser el apoyo estable y confiable del grupo",
                    personalityWeights = mapOf(Personality.STEADY to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Analizar datos y optimizar estrategias",
                    personalityWeights = mapOf(Personality.CONSCIENTIOUS to ANSWER_SCORE)
                )
            )
        ),

        Question(
            id = 3,
            text = "¿Qué tipo de misiones secundarias te atraen más?",
            options = listOf(
                AnswerOption(
                    text = "Las que otorgan mejoras poderosas o progreso inmediato",
                    personalityWeights = mapOf(Personality.DOMINANT to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Las que incluyen personajes interesantes o buena narrativa",
                    personalityWeights = mapOf(Personality.INFLUENTIAL to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Las que permiten ayudar o aportar al mundo del juego",
                    personalityWeights = mapOf(Personality.STEADY to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Las que requieren precisión, acertijos o colección",
                    personalityWeights = mapOf(Personality.CONSCIENTIOUS to ANSWER_SCORE)
                )
            )
        ),

        Question(
            id = 4,
            text = "Cuando enfrentas un problema, ¿cómo reaccionas?",
            options = listOf(
                AnswerOption(
                    text = "Actúo de inmediato para resolverlo rápido",
                    personalityWeights = mapOf(Personality.DOMINANT to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Busco opiniones y apoyo de otras personas",
                    personalityWeights = mapOf(Personality.INFLUENTIAL to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Me tomo un momento y procedo con calma",
                    personalityWeights = mapOf(Personality.STEADY to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Analizo todas las opciones antes de actuar",
                    personalityWeights = mapOf(Personality.CONSCIENTIOUS to ANSWER_SCORE)
                )
            )
        ),

        Question(
            id = 5,
            text = "En juegos multijugador, prefieres:",
            options = listOf(
                AnswerOption(
                    text = "El rol que toma decisiones rápidas en combate",
                    personalityWeights = mapOf(Personality.DOMINANT to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "El rol que fomenta trabajo en equipo y comunicación",
                    personalityWeights = mapOf(Personality.INFLUENTIAL to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "El rol de apoyo que da estabilidad al grupo",
                    personalityWeights = mapOf(Personality.STEADY to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "El rol analítico que estudia tácticas",
                    personalityWeights = mapOf(Personality.CONSCIENTIOUS to ANSWER_SCORE)
                )
            )
        ),

        Question(
            id = 6,
            text = "Al elegir qué jugar después, priorizas:",
            options = listOf(
                AnswerOption(
                    text = "Un juego competitivo donde pueda destacar",
                    personalityWeights = mapOf(Personality.DOMINANT to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Un juego con una comunidad activa y social",
                    personalityWeights = mapOf(Personality.INFLUENTIAL to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Un juego familiar o estable que me haga sentir cómodo",
                    personalityWeights = mapOf(Personality.STEADY to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Un juego complejo que permita mejorar habilidades",
                    personalityWeights = mapOf(Personality.CONSCIENTIOUS to ANSWER_SCORE)
                )
            )
        ),

        Question(
            id = 7,
            text = "En tu tiempo libre, prefieres actividades que:",
            options = listOf(
                AnswerOption(
                    text = "Te reten y ayuden a mejorar rápidamente",
                    personalityWeights = mapOf(Personality.DOMINANT to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Te conecten con otras personas",
                    personalityWeights = mapOf(Personality.INFLUENTIAL to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Sean relajantes y predecibles",
                    personalityWeights = mapOf(Personality.STEADY to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Permitan profundizar y aprender a fondo",
                    personalityWeights = mapOf(Personality.CONSCIENTIOUS to ANSWER_SCORE)
                )
            )
        ),

        Question(
            id = 8,
            text = "Cuando un juego se vuelve difícil, tu reacción típica es:",
            options = listOf(
                AnswerOption(
                    text = "Persistir hasta superarlo por determinación",
                    personalityWeights = mapOf(Personality.DOMINANT to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Buscar consejos y experiencias de otros jugadores",
                    personalityWeights = mapOf(Personality.INFLUENTIAL to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Tomar una pausa y volver más tarde con calma",
                    personalityWeights = mapOf(Personality.STEADY to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Estudiar patrones y mecánicas a fondo",
                    personalityWeights = mapOf(Personality.CONSCIENTIOUS to ANSWER_SCORE)
                )
            )
        ),

        Question(
            id = 9,
            text = "Cuando tomas decisiones importantes, tiendes a:",
            options = listOf(
                AnswerOption(
                    text = "Actuar rápido siguiendo tu intuición",
                    personalityWeights = mapOf(Personality.DOMINANT to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Conversarlo con otras personas primero",
                    personalityWeights = mapOf(Personality.INFLUENTIAL to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Pensar en cómo afectará a quienes quieres",
                    personalityWeights = mapOf(Personality.STEADY to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Hacer un análisis detallado antes de decidir",
                    personalityWeights = mapOf(Personality.CONSCIENTIOUS to ANSWER_SCORE)
                )
            )
        ),

        Question(
            id = 10,
            text = "¿Qué valoras más en un videojuego?",
            options = listOf(
                AnswerOption(
                    text = "La competencia y el desafío",
                    personalityWeights = mapOf(Personality.DOMINANT to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Los personajes y la historia",
                    personalityWeights = mapOf(Personality.INFLUENTIAL to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "La atmósfera tranquila y consistente",
                    personalityWeights = mapOf(Personality.STEADY to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "La profundidad mecánica y diseño detallado",
                    personalityWeights = mapOf(Personality.CONSCIENTIOUS to ANSWER_SCORE)
                )
            )
        ),

        Question(
            id = 11,
            text = "En situaciones sociales nuevas, tiendes a:",
            options = listOf(
                AnswerOption(
                    text = "Tomar la iniciativa y comenzar la conversación",
                    personalityWeights = mapOf(Personality.DOMINANT to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Conectar rápidamente con nuevas personas",
                    personalityWeights = mapOf(Personality.INFLUENTIAL to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Observar primero hasta sentirte cómodo",
                    personalityWeights = mapOf(Personality.STEADY to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Analizar el ambiente antes de involucrarte",
                    personalityWeights = mapOf(Personality.CONSCIENTIOUS to ANSWER_SCORE)
                )
            )
        ),

        Question(
            id = 12,
            text = "¿Cómo prefieres organizar tu biblioteca de juegos?",
            options = listOf(
                AnswerOption(
                    text = "Conservar solo los juegos que uso activamente",
                    personalityWeights = mapOf(Personality.DOMINANT to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Mantener una colección variada para compartir",
                    personalityWeights = mapOf(Personality.INFLUENTIAL to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Ordenarla por rutina o fecha de adquisición",
                    personalityWeights = mapOf(Personality.STEADY to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Clasificar con etiquetas y categorías detalladas",
                    personalityWeights = mapOf(Personality.CONSCIENTIOUS to ANSWER_SCORE)
                )
            )
        )
    )

    val tiebreakerQuestions = listOf(
        Question(
            id = 101,
            text = "En un proyecto grupal, ¿qué rol te representa mejor?",
            options = listOf(
                AnswerOption(
                    text = "Tomar el liderazgo y guiar al equipo hacia objetivos claros",
                    personalityWeights = mapOf(Personality.DOMINANT to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Crear un ambiente motivador y mantener la comunicación",
                    personalityWeights = mapOf(Personality.INFLUENTIAL to ANSWER_SCORE)
                )
            )
        ),
        Question(
            id = 102,
            text = "Al organizar tu día, ¿qué enfoque te describe mejor?",
            options = listOf(
                AnswerOption(
                    text = "Seguir una rutina constante que te dé estabilidad",
                    personalityWeights = mapOf(Personality.STEADY to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Planificar y revisar cada detalle antes de avanzar",
                    personalityWeights = mapOf(Personality.CONSCIENTIOUS to ANSWER_SCORE)
                )
            )
        ),
        Question(
            id = 103,
            text = "Cuando enfrentas un desafío complejo, ¿cómo reaccionas?",
            options = listOf(
                AnswerOption(
                    text = "Actúas rápido y ajustas la estrategia sobre la marcha",
                    personalityWeights = mapOf(Personality.DOMINANT to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Analizas todas las variables antes de tomar una decisión",
                    personalityWeights = mapOf(Personality.CONSCIENTIOUS to ANSWER_SCORE)
                )
            )
        ),
        Question(
            id = 104,
            text = "En tu tiempo libre, ¿qué disfrutas más?",
            options = listOf(
                AnswerOption(
                    text = "Socializar, conocer gente y participar en actividades grupales",
                    personalityWeights = mapOf(Personality.INFLUENTIAL to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Actividades tranquilas, predecibles y sin presión social",
                    personalityWeights = mapOf(Personality.STEADY to ANSWER_SCORE)
                )
            )
        ),
        Question(
            id = 100,
            text = "¿Qué cualidad sientes que te representa mejor?",
            options = listOf(
                AnswerOption(
                    text = "Tu determinación para superar obstáculos y avanzar",
                    personalityWeights = mapOf(Personality.DOMINANT to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Tu capacidad para conectar, comunicar y motivar",
                    personalityWeights = mapOf(Personality.INFLUENTIAL to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Tu constancia y confiabilidad en cualquier situación",
                    personalityWeights = mapOf(Personality.STEADY to ANSWER_SCORE)
                ),
                AnswerOption(
                    text = "Tu foco en la precisión, calidad y análisis cuidadoso",
                    personalityWeights = mapOf(Personality.CONSCIENTIOUS to ANSWER_SCORE)
                )
            )
        )
    )
}