# Taskforge Project Plan

## Product Idea

Taskforge is a habit-tracking Android app built in Java with the Android SDK. The app helps users create habits, track daily progress, manage monthly goals, earn themed medals, and stay motivated through a friend community.

The core idea is simple: users do not only track habits privately; they can also post what they completed today and let trusted friends see, encourage, and gently hold them accountable.

## Core Features

### Habit Tracking

- Create, edit, pause, and delete habits.
- Track daily completion status.
- Support habit frequency, such as daily, selected weekdays, or monthly target count.
- Show habit lists with RecyclerView for efficient rendering.
- Display streaks, completion rate, and monthly progress.

### Goal Rules

- Monthly targets, such as "read 20 days this month."
- Skip-day allowance, such as "2 skip days per month."
- Automatic goal status:
  - On track
  - At risk
  - Completed
  - Missed
- Local calculations based on check-in records and current date.

### Reward System

- Earn medals based on monthly performance.
- Generate themed badges with Android Canvas APIs.
- Badge themes can change by month, season, or achievement type.
- Example medals:
  - Bronze: 50% monthly completion
  - Silver: 75% monthly completion
  - Gold: 90% monthly completion
  - Perfect Month: 100% completion

### Reminders

- Schedule habit reminders with AlarmManager.
- Receive reminder events through BroadcastReceiver.
- Keep reminder work lightweight so the main UI thread is not blocked.
- Allow users to turn reminders on or off per habit.

### Friend Community

- Add friends by username, invite code, or email.
- View a private community feed containing posts from added friends only.
- Post a daily update like:
  - "I finished my workout today."
  - "Studied Java for 45 minutes."
  - "Skipped sugar today."
- React to friends' posts with encouragement.
- Add simple comments for accountability.
- Optional daily prompt: "What did you complete today?"

### Accountability Features

- Friend visibility settings:
  - Private habit
  - Visible progress only
  - Visible posts only
  - Visible habit and progress
- Accountability partner mode for selected friends.
- Weekly friend summary, such as "3 friends completed their goals today."
- Gentle missed-day reminder, not public shaming.

## Suggested Screens

### MainActivity

Hosts the bottom navigation and coordinates top-level fragments.

### HomeFragment

Displays today's habit list, quick check-in buttons, and current streaks.

### HabitDetailFragment

Shows habit history, monthly progress, skip-day usage, and reminder settings.

### AddEditHabitActivity

Handles habit creation and editing.

### RewardsFragment

Displays earned medals, locked medals, and generated Canvas badge previews.

### CommunityFragment

Shows posts from friends. Users can create a daily update, react, and comment.

### FriendsFragment

Manages friend search, friend requests, accepted friends, and privacy settings.

### SettingsFragment

Contains app preferences, notification settings, sync settings, and account controls.

## Data Model

### Local SQLite Tables

`habits`

| Field | Type | Notes |
| --- | --- | --- |
| id | INTEGER PRIMARY KEY | Local habit id |
| title | TEXT | Habit name |
| description | TEXT | Optional detail |
| frequency_type | TEXT | daily, weekdays, monthly |
| monthly_target | INTEGER | Target count per month |
| skip_allowance | INTEGER | Allowed skipped days |
| reminder_enabled | INTEGER | 0 or 1 |
| reminder_time | TEXT | HH:mm |
| created_at | INTEGER | Epoch millis |
| archived_at | INTEGER | Nullable |

`habit_checkins`

| Field | Type | Notes |
| --- | --- | --- |
| id | INTEGER PRIMARY KEY | Local check-in id |
| habit_id | INTEGER | References habits.id |
| checkin_date | TEXT | yyyy-MM-dd |
| status | TEXT | completed, skipped, missed |
| note | TEXT | Optional user note |
| created_at | INTEGER | Epoch millis |

`medals`

| Field | Type | Notes |
| --- | --- | --- |
| id | INTEGER PRIMARY KEY | Local medal id |
| habit_id | INTEGER | References habits.id |
| month | TEXT | yyyy-MM |
| medal_type | TEXT | bronze, silver, gold, perfect |
| earned_at | INTEGER | Epoch millis |

`community_posts`

| Field | Type | Notes |
| --- | --- | --- |
| id | TEXT PRIMARY KEY | Local or Firebase id |
| author_id | TEXT | User id |
| content | TEXT | Daily update text |
| habit_id | INTEGER | Optional related habit |
| post_date | TEXT | yyyy-MM-dd |
| visibility | TEXT | friends, accountability_partner |
| created_at | INTEGER | Epoch millis |
| synced | INTEGER | 0 or 1 |

### SharedPreferences

- First launch completed
- Current user id
- Notification preference
- Theme preference
- Last sync timestamp

## Firebase Structure

Recommended Firebase Realtime Database paths:

```text
users/{userId}
friends/{userId}/{friendId}
friendRequests/{targetUserId}/{requestId}
posts/{userId}/{postId}
postReactions/{postId}/{userId}
postComments/{postId}/{commentId}
```

The app should keep local SQLite as the source of truth for core habits, then sync selected community and account data with Firebase.

## Implementation Milestones

### Milestone 1: Local MVP

- Create Android Java project.
- Build Home screen with RecyclerView.
- Start with a manual quantitative tracking slice: Drink Water, 2000 ml/day, quick log buttons.
- Add habit creation and local SQLite persistence.
- Support daily check-ins.
- Show basic monthly progress.

### Milestone 2: Goal Logic

- Add monthly targets.
- Add skip-day allowance.
- Calculate streaks and completion rate.
- Add habit detail screen.

### Milestone 3: Rewards

- Add medal rules.
- Store earned medals locally.
- Render simple badges with Canvas.
- Show rewards gallery.

### Milestone 4: Reminders

- Add reminder settings per habit.
- Schedule notifications with AlarmManager.
- Handle reminder events with BroadcastReceiver.

### Milestone 5: Community

- Add Firebase account identity.
- Add friend requests and accepted friends.
- Add private friend feed.
- Add daily posts, reactions, and comments.

### Milestone 6: Sync and Polish

- Sync community posts across devices.
- Add conflict handling for habit check-ins if multi-device habit sync is enabled.
- Improve privacy controls.
- Add empty states, loading states, and error states.

## Resume-Friendly Description

Developed a Java Android habit-tracking app using Android SDK, modular Activities, Fragments, and RecyclerView to support efficient habit list rendering, daily check-ins, and stateful goal tracking.

Implemented monthly target logic, skip-day allowances, reward medals, local SQLite persistence, SharedPreferences settings, AlarmManager reminders, and Firebase-backed friend community features for accountability and multi-device engagement.

## Notes on Scope

The friend community is valuable, but it should be built after the local habit system works well. The local tracker is the foundation; community features become much easier once habits, check-ins, and monthly progress are reliable.
