import djchoices
import uuid
from django.contrib.auth.models import User
from django.core import validators
from django.core.validators import MaxValueValidator, MinValueValidator
from django.db import models
from django.db.models.aggregates import Avg, Sum
from django.db.models.signals import post_save
from django.dispatch import receiver


class ModeType(djchoices.DjangoChoices):
    TOURISTIC = djchoices.ChoiceItem()
    SPORTY = djchoices.ChoiceItem()


class ProfileType(djchoices.DjangoChoices):
    TOURISTIC = djchoices.ChoiceItem()
    SPORTY = djchoices.ChoiceItem()
    BOTH = djchoices.ChoiceItem()


class Route(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    publisher = models.ForeignKey(User)

    date_published = models.DateTimeField(auto_now_add=True)
    last_updated = models.DateTimeField(auto_now=True)

    title = models.CharField(max_length=64, unique=True, validators=[validators.MinLengthValidator(3), ])
    description = models.CharField(max_length=200)

    mode = models.CharField(max_length=16, choices=ModeType.choices)

    difficulty = models.IntegerField(validators=[
        MinValueValidator(1), MaxValueValidator(5)
    ])

    # markers list found in Marker model

    @property
    def rating(self):
        average = RouteRating.objects.filter(route=self).aggregate(Avg('rating'))["rating__avg"]
        return 0.0 if average is None else average

    def __str__(self):
        count = self.markers.count()
        return "%s: %s (%d marker%s)" % (self.title, self.description, count, "" if count == 1 else "s")


class CompletedRoute(models.Model):
    user = models.ForeignKey(User)
    route = models.ForeignKey(Route)

    when = models.DateTimeField(auto_now_add=True)
    distance = models.FloatField()
    duration = models.DurationField()

    def __str__(self):
        return "%s completed by %s on %s" % (self.route, self.user, self.when)


def get_file_path(instance, filename):
    return "markers/%s_%s" % (instance.id, filename)


class Marker(models.Model):
    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    title = models.CharField(max_length=64, unique=True)
    description = models.CharField(max_length=200)
    full_description = models.TextField()

    latitude = models.DecimalField(max_digits=9, decimal_places=6)
    longitude = models.DecimalField(max_digits=9, decimal_places=6)

    clue = models.CharField(max_length=256, blank=True)
    target_image = models.ImageField(upload_to=get_file_path, null=True)
    target_texture = models.FileField(upload_to=get_file_path, null=True)

    zone_radius = models.FloatField()

    routes = models.ManyToManyField(Route, related_name='markers')

    def __str__(self):
        return "%s: %s" % (self.title, self.description)


class UserProfile(models.Model):
    user = models.OneToOneField(User, primary_key=True, on_delete=models.CASCADE)

    profile_type = models.CharField(max_length=16, choices=ProfileType.choices)
    birth_date = models.DateField(null=True)
    email_verified = models.BooleanField(default=False)

    @property
    def total_distance(self):
        total = CompletedRoute.objects.filter(user=self.user).aggregate(Sum('distance'))['distance__sum']
        return 0.0 if total is None else total

    @property
    def total_completed(self):
        return CompletedRoute.objects.filter(user=self.user).count()

    @property
    def total_duration(self):
        total = CompletedRoute.objects.filter(user=self.user).aggregate(Sum('duration'))['duration__sum']
        return 0.0 if total is None else total.total_seconds()

    def __str__(self):
        return str(self.user)


class RouteRating(models.Model):
    class Meta:
        unique_together = ('user', 'route')

    user = models.ForeignKey(User)
    route = models.ForeignKey(Route)
    rating = models.IntegerField(validators=[
        MinValueValidator(1),
        MaxValueValidator(5)
    ])

    def __str__(self):
        return "Rating of %s for route %s by %s" % (
            self.rating, self.route.id, self.user.get_full_name() or self.user.username)


@receiver(post_save, sender=User)
def create_user_profile(sender, instance, created, **kwargs):
    if created:
        UserProfile.objects.create(user=instance)


@receiver(post_save, sender=User)
def save_user_profile(sender, instance, **kwargs):
    instance.userprofile.save()
