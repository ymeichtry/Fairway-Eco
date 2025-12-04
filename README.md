# Fairway-Eco

## CI/CD

- **Hinzugefügte Workflows**:
	- `./github/workflows/ci.yml` — Führt den `github/super-linter` aus und startet bedingte Tests für Node.js (wenn `package.json` vorhanden) sowie Python (wenn `requirements.txt` oder `pyproject.toml` vorhanden). Läuft bei `push` und `pull_request` auf dem Branch `main`.
	- `./github/workflows/docker-deploy.yml` — Baut und pusht ein Docker-Image auf Docker Hub bei Pushs auf `main`, falls eine `Dockerfile` vorhanden ist.

- **Repository-Secrets für Docker**:
	- `DOCKERHUB_USERNAME` — Docker Hub Benutzername
	- `DOCKERHUB_TOKEN` — Docker Hub Access Token oder Passwort

- **Wie die Workflows funktionieren (kurz)**:
	- Der CI-Workflow lintet das Repo und versucht, vorhandene Test-Suites für Node/Python auszuführen; wenn keine Testdateien vorhanden sind, werden die entsprechenden Schritte übersprungen.
	- Der Docker-Workflow prüft, ob eine `Dockerfile` existiert; falls ja, wird ein Multi-Arch-Image gebaut und zu Docker Hub gepusht (Tags: Commit-Kurz-Hash und `latest`).

- **Nächste Schritte**:
	- Falls Sie Docker-Images pushen wollen: setzen Sie die oben genannten Secrets in den Repository-Einstellungen (`Settings` → `Secrets and variables` → `Actions`).
	- Fügen Sie Tests für Ihr Projekt hinzu (`npm test` / `pytest`) damit CI aussagekräftig wird.

Wenn Sie möchten, kann ich noch weitere Workflows (z.B. Release-Tagging, GitHub Pages oder Deployment in Cloud-Provider) vorbereiten.
