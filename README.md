# Taskforge

Taskforge is a Java Android habit-tracking app concept focused on personal consistency, monthly goals, rewards, reminders, and friend-based accountability.

The planned app starts with local habit tracking and grows into a lightweight community where friends can share what they completed today, react to each other's progress, and stay accountable together.

## First App Slice

The current Android app skeleton starts with one local quantitative habit:

- Seeded habits: Drink Water, Exercise, and Sleep
- Daily goals: 2000 ml water, 30 min exercise, 8 hr sleep
- Manual logs: quick amount buttons per habit
- Local persistence: SQLite through `SQLiteOpenHelper`
- UI: Java Activity with RecyclerView cards

See [Project Plan](docs/PROJECT_PLAN.md) for the feature roadmap, screen structure, data model, and implementation milestones.

## Open In Android Studio

Open this repository folder in Android Studio, let Gradle sync, then run the `app` configuration on an emulator or Android device.
