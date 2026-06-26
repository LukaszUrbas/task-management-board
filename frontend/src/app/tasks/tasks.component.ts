import { Component, OnInit } from '@angular/core';
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
  tasks: Task[] = [];
  loading = true;
  error: string | null = null;

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.taskService.getTasks().subscribe({
      next: (data) => {
        this.tasks = data;
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Nie udało się połączyć z backendem: ' + err.message;
        this.loading = false;
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

