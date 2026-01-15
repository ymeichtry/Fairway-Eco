# GitHub Secrets Setup Guide

Für die CI/CD-Pipelines benötigst du folgende Secrets in deinem GitHub Repository. Gehe zu:
**Settings → Secrets and variables → Actions** und füge diese ein:

## Required Secrets

### 1. Docker Hub Credentials (für Docker-Push)

**Optional**, aber notwendig wenn du Docker Images zu Docker Hub pushen möchtest.

#### `DOCKERHUB_USERNAME`
- **Was**: Dein Docker Hub Benutzername
- **Beispiel**: `myusername`
- **Wie**: 
  1. Gehe zu https://hub.docker.com
  2. Login oder registriere dich
  3. Dein Username findest du im Profil

#### `DOCKERHUB_TOKEN`
- **Was**: Docker Hub Access Token (nicht dein Passwort!)
- **Beispiel**: `dckr_pat_abc123...`
- **Wie**:
  1. Gehe zu https://hub.docker.com/settings/security
  2. Klicke "New Access Token"
  3. Gib einen Namen ein (z.B. `fairway-eco-github`)
  4. Setze Permissions: `Read, Write, Delete`
  5. Kopiere den Token (wird nur einmal angezeigt!)

---

## Optional Secrets (für weitere Funktionen)

Falls du später zusätzliche Features hinzufügst (z.B. Deployment in AWS, Azure, Slack Notifications):

### AWS Credentials (für Deployment)
- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`

### Slack Notifications
- `SLACK_WEBHOOK_URL`

---

## Workflow Behavior

- **CI Workflow** (`.github/workflows/ci.yml`):
  - Triggert bei Push oder Pull Request auf `main`
  - Baut Backend (Maven) und Frontend (Node)
  - Lädt Artifacts hoch (JAR & dist)
  - Braucht **KEINE Secrets**

- **Docker Workflow** (`.github/workflows/docker-deploy.yml`):
  - Triggert nach erfolgreichen CI-Run oder bei Push auf `main`
  - Baut Docker Images für Backend & Frontend
  - Pusht zu Docker Hub (optional, wenn Secrets gesetzt)
  - Wenn Docker-Secrets leer: Builds lokal, pusht nicht (kein Error)

---

## How to Add Secrets

### Via GitHub Web UI
1. Gehe zu `Settings` → `Secrets and variables` → `Actions`
2. Klicke `New repository secret`
3. **Name**: `DOCKERHUB_USERNAME` (exakt wie oben)
4. **Value**: Dein Username
5. Klicke `Add secret`

### Via GitHub CLI
```bash
gh secret set DOCKERHUB_USERNAME --body "myusername"
gh secret set DOCKERHUB_TOKEN --body "dckr_pat_abc123..."
```

---

## Verify Setup

Nach dem Hinzufügen der Secrets:
1. Mache einen Push auf `main`
2. Gehe zu `Actions` in deinem GitHub Repo
3. Beobachte die Workflows:
   - ✓ CI sollte immer durchlaufen
   - ✓ Docker sollte durchlaufen (mit Warnung, wenn secrets leer)

---

## Troubleshooting

**Problem**: "authentication denied" beim Docker Push
- **Lösung**: Prüfe `DOCKERHUB_TOKEN` (nicht das Passwort!)

**Problem**: CI bricht ab bei Frontend Build
- **Lösung**: Stelle sicher, dass `npm ci` & `npm run build` lokal funktionieren

**Problem**: Backend Build schlägt fehl
- **Lösung**: Prüfe, dass JDK 17 lokal funktioniert (`mvn -v`)

---

## Next Steps

1. Setze die Docker-Secrets (optional)
2. Mache einen Commit & Push
3. Beobachte die Workflows in GitHub Actions
4. Später: Einstellen von Deployment-Zielen (AWS, Azure, etc.)
