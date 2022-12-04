<!-- # Title -->
# Room
![Demo](https://media.discordapp.net/attachments/655489748885831713/1048705196718567455/f99e0b812f21d685b09a01fa60ec1cd3.png)


<!-- # Short Description -->

>- The application was designed to create *reservations* for users in a specific hostel.
>- The user can register to access the *reservation* system and can choose all the specitications, such as **date**, which **currency** you want to 
use as payment and **number of beds**, providing the user the **total value** instantly converted.
>- The user can *delete* and *edit* the reservation any time he wants.

This application was designed to supply the needed requisites from a *Android Code Challenge*, designing an application that provides a platform which the
user can **register** himself to access the system and be able to make **reservations** for a specific hostel, with *Google Firebase* as a **database**. \
The application provides to the user, realtime *exchange rates* for different *currencies*, receiving the total value of his **reservation** instantly. 


<!-- # Badges -->
<div style="display: inline_block"><br>
    <img height="30" width="40" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/androidstudio/androidstudio-original.svg">
    <img height="30" width="40" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/kotlin/kotlin-original.svg">
    <img height="30" width="40" src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/firebase/firebase-plain.svg">
</div>

---

# Tags

`Android Studio` `Kotlin` `Firebase` `OkHTTP` `Coroutines` `MVVM` `Retrofit` `Hilt`

---

# Android Code Challenge
## Rules: 
> Description
>- Bed values defined before
>- Be able to **ad**, **remove** or **edit** the *reservation* possibilities inside the patterns, connecting the *bed value* with the room capacity
>- Button that displays the total value in different *currencies*
>- Use the requested *API* to convert the *currencies*


---

# Demo

Register             |  Login
:-------------------------:|:-------------------------:
![](https://media.discordapp.net/attachments/655489748885831713/1048755285986127882/ezgif.com-gif-maker.gif)  |  ![](https://media.discordapp.net/attachments/655489748885831713/1048754492843249794/ezgif.com-gif-maker.gif)


>- **Register** and **Login** with *Google Firebase*





Create Reservation         |    Delete Reservation     |  Update Reservation  
:-------------------------:|:-------------------------:|:-------------------------:
![](https://media.discordapp.net/attachments/655489748885831713/1048754836646137886/ezgif.com-gif-maker.gif)  |   ![](https://media.discordapp.net/attachments/655489748885831713/1048756524224360488/ezgif.com-gif-maker.gif)  |  ![](https://media.discordapp.net/attachments/655489748885831713/1048754041179619398/UPDATE.gif)


>- Create a new **reservation**, registering it on *Google Firebase*
>- Delete one **reservation** from the system, completly
>- Update your **reservation** receiving real-time new value


---

# Code Example
```kotlin
private fun configCardView() = with(binding) {
        arrowDown6.setOnClickListener {
            if (hiddenView6.visibility == View.VISIBLE) {
                hiddenView6.hide()
                arrowDown6.setImageResource(R.drawable.ic_arrow_down_24)
            } else {
                TransitionManager.beginDelayedTransition(cardView6, AutoTransition())
                hiddenView6.show()
                arrowDown6.setImageResource(R.drawable.ic_arrow_up_24)
            }
        }
        arrowDown8.setOnClickListener {
            if (hiddenView8.visibility == View.VISIBLE) {
                hiddenView8.hide()
                arrowDown8.setImageResource(R.drawable.ic_arrow_down_24)
            } else {
                TransitionManager.beginDelayedTransition(cardView8, AutoTransition())
                hiddenView8.show()
                arrowDown8.setImageResource(R.drawable.ic_arrow_up_24)
            }
        }
        arrowDown12.setOnClickListener {
            if (hiddenView12.visibility == View.VISIBLE) {
                hiddenView12.hide()
                arrowDown12.setImageResource(R.drawable.ic_arrow_down_24)
            } else {
                TransitionManager.beginDelayedTransition(cardView12, AutoTransition())
                hiddenView12.show()
                arrowDown12.setImageResource(R.drawable.ic_arrow_up_24)
            }
        }
    }
```

A small code designed to hide and show the selected CardView. It creates a small animation and a nice design for the user interaction. 

---

# Libraries

>- [Timber](https://github.com/JakeWharton/timber)
>- [Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle)
>- [Coroutines](https://developer.android.com/kotlin/coroutines?hl=pt-br)
>- [KTX](https://developer.android.com/kotlin/ktx)
>- [Retrofit](https://square.github.io/retrofit/)
>- [OkHTTP](https://square.github.io/okhttp/)
>- [Navigation Components](https://developer.android.com/guide/navigation)
>- [Hilt](https://dagger.dev/hilt/)
>- [Picasso](https://github.com/square/picasso)
>- [Material Card View](https://github.com/prolificinteractive/material-calendarview)
>- [Google Firebase](https://firebase.google.com)
>- [Exchange Rate API](https://exchangeratesapi.io)

---

# Contributors

- [Thiago Rodrigues](https://www.linkedin.com/in/tods/)
