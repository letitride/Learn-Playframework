# サンプルデータベース

```
$  docker run --name play-sample-db -p 5432:5432 -e POSTGRES_USER=play -e POSTGRES_PASSWORD=play -e POSTGRES_DB=playdb -v $(pwd)/pgdata:/var/lib/postgresql/data -d postgres:12.2
```