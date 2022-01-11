<div id="top"></div>
  
<!-- PROJECT LOGO -->
<br />
 

<h3 align="center">Crash Dataset Project</h3>
 



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project 

The project is about Passenger Plane Crashes dataset. You are going to develop a UI (console based) which will help the user to work on the dataset. The main functionalities the program needs to provide are the following:
1. List all the entities (20 points)  
  a. List all the fields of each entity  
  b. List only the selected fields of each entity  
  c. List entities based on the range of rows (e.g., range is given, 5 100)  
  d. Note: all lists should be followed by the number of entities listed.  
2. Sort the entities (30 points)  
  a. Based on any field  
  b. In any order (i.e., ASC, DESC)  
3. Search entity(ies) based on any given field and value (15 points)  
  a. string fields and values must be checked based on contains not exact equality.  
  b. non-string fields and values must be checked based on exact equality.  
4. List column names (5 points)  
5. Filter entities (30 points)  
  a. Based on any given field or set of fields and according to some rules:  
  b. string fields  
    i. starts with  
    ii. ends with  
    iii. contains  
    iv. null  
  c. date, time, and numeric fields  
    i. equal (eq)  
    ii. greater than (gt)  
    iii. less than (lt)  
    iv. greater and equal to (ge)  
    v. less and equal to (le)  
    vi. between (bt)  
    vii. null  
  d. date and time  
    i. in a specific year (y)  
    ii. in a specific month (m)  
    iii. in a specific day (d)  
  e. [IF you take clustId as a Boolean value (i.e., is high fatality]  
   i. Equal (eq) [true or false]  
  f. Examples:  
    i. select all the crashes happened in 1962 which has death rate (opposite of survivalRate) greater than 50%.  
    ii. select all the crashes happened any time in 20:00 - 00:00 by operator Aeroflot  
    iii. select all the crashes happened in Cameroon  
    iv. select all the crashes which caused more than 20 dead on the ground  
<p align="right">(<a href="#top">back to top</a>)</p>



### Built With

* Java
* Apache Commons
* opencsv
 

 

### Installation

1. Upload project to your favourite IDE
2. Follow the UI, everything is user-friendly :)
  
 
<p align="right">(<a href="#top">back to top</a>)</p>


 
