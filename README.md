# spring-boot-docker
dockerised spring boot app


Sample curl for the post and get. 

POST : saves details of an actor
curl --location --request POST 'http://host:port/events' \
--header 'Content-Type: application/json' \
--data-raw '{
  "id":4155134610,
  "type":"PushEvent",
  "actor":{
    "id":2790313,
    "login":"daniel33",
    "avatar_url":"https://avatars.com/2790311"
  },
  "repo":{
    "id":352806,
    "name":"johnbolton/exercitationem",
    "url":"https://github.com/johnbolton/exercitationem"
  },
  "created_at":"2020-10-02 06:13:31"
}'

GET: gets details of all actors
curl --location --request GET 'http://host:port/events'
