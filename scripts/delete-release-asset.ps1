$Token = (Get-Content "C:\Users\edwar\MultiAppShare-\scripts\.env.local" | Select-String "GITHUB_TOKEN=(.+)").Matches.Groups[1].Value.Trim()
$headers = @{ Authorization = "Bearer $Token"; Accept = "application/vnd.github+json" }
$base = "https://api.github.com/repos/edwardlthompson/MultiAppShare-"
$release = Invoke-RestMethod ($base + "/releases/tags/v1.7.4") -Headers $headers
$asset = $release.assets | Where-Object { $_.name -eq "app-release.apk" }
if ($asset) {
    Invoke-RestMethod -Method Delete -Uri $asset.url -Headers $headers
    Write-Host "Success"
}
