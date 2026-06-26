import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TaskService } from './task.service';
import { Task } from './task.model';

@Component({
  selector: 'app-tasks',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './tasks.component.html',
  styleUrl: './tasks.component.scss'
})
export class TasksComponent implements OnInit {
  tasks = signal<Task[]>([]);
  loading = signal(true);
  error = signal<string | null>(null);

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.taskService.getTasks().subscribe({
      next: (data) => {
        this.tasks.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('Nie udało się połączyć z backendem: ' + err.message);
        this.loading.set(false);
      }
    });
  }

  statusLabel(status: Task['status']): string {
    const map: Record<Task['status'], string> = {
      TODO: 'Do zrobienia',
      IN_PROGRESS: 'W trakcie',
      DONE: 'Gotowe'
    };
    return map[status];
  }
}

