LandedEstatesDB
===============


## Database setup

```
CREATE DATABASE landedestates;
CREATE USER 'ledb'@'%' IDENTIFIED BY 'xxxxxx';
GRANT ALL PRIVILEGES ON ledb.* TO 'ledb'@'%';
```

Replace 'xxxxxx' with the actual password.