# sports-leagues-manager
restfull services for sports leagues manager resources

1. Save League:
Method: PUT
Content-Type: application/json
URL: http://localhost:9002/sports/v1/sports/cricket/leagues
Payload:
{
    "leagueId": 1,
    "sport": "cricket",
    "name": "IPL",
    "city": "Mumbai",
    "state": "MH",
    "country": "India"
}

2. GET Leagues:
Method: GET
Content-Type: application/json
URL: http://localhost:9002/sports/v1/sports/cricket/leagues

Response:
[
    {
        "leagueId": 1,
        "sport": "cricket",
        "name": "IPL",
        "city": "Mumbai",
        "state": "MH",
        "country": "India"
    },
    {
        "leagueId": 2,
        "sport": "cricket",
        "name": "ICL",
        "city": "Sydney",
        "state": "Sydney",
        "country": "Australia"
    }
]

3. Get League by sport and leagueid
Method: GET
Content-Type: application/json
URL: http://localhost:9002/sports/v1/sports/cricket/leagues/1
Response:
{
    "leagueId": 1,
    "sport": "cricket",
    "name": "IPL",
    "city": "Mumbai",
    "state": "MH",
    "country": "India"
}
