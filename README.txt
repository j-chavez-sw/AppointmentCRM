C195 Performance Assessment
Julian Chavez
Student ID: #000966293

Requirement Notes:

General: My project is themed as a photography business scheduler. It has 5 types of appointments in 5 available cities.
Business is based in New York, NY.

A: LOGIN
Login and alert text changes into ENGLISH('en') or chinese ('zh'). The language resources are found in the
Languages>Resource Bundle directory

B: Customer
Adds, updates, and deletes Customer entries as required.

C: Appointments
Adds, updates, and deletes Appointment entries as required.
Appointments are fixed at 1 hour in length to mitigate appointment overlaps.

D: Calendar
Appointments can be viewed on the main dashboard screen. Select a date(using the date picker)
and then click By Month or By Week button to get selected results.

E: Time Zones
Adjusts time zones as required. Reads GMT time from database and converts it to the local default. Business is based in
New York, NY so business times are based there as well. Business hours are 0800-1500 New York time.

F: Exception Controls
Program utilizes throws, try/catch blocks, and try with resources exception controls. Can be found in QueryDB class

Scheduling an appointment outside of business house: Appointment hours are listed by availability. User is unable to
select an appointment time that is not acceptable.

Overlapping appointments: Overlap is prevented by limiting appointments to 1 hour blocks. When an appointment slot is
taken it is no longer available for selection so overlap is prevented.

Entering incorrect date: Program will trigger an alert box if necessary fields are not filled out properly. Project
requirements did not specify how accurate data entered must be.

Entering incorrect password/username: Program will trigger alert box if credentials do not match users in database

G: Lambda Expressions
This program uses Lambda Expressions using Predicate, Consumer, and ChangeListener.

H: 15 Minute Alert
Meets project requirements.

I: Reports
Found on main dashboard screen.
Report 1: Number of appointments by type by user selected month.
Report 2: Appointments for each user selected consultant.
Report 3: Shows the user activity via Requirement J log file.

J: Log File
User activity saved in logins.txt in C195ChavezJulian directory.
